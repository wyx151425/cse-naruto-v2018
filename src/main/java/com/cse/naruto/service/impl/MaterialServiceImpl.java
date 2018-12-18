package com.cse.naruto.service.impl;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.PageContext;
import com.cse.naruto.model.User;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.service.MaterialService;
import com.cse.naruto.util.Constant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/11/29
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * 通过BOM导入物料数据列表
     *
     * @param file 前端提交的柴油机BOM
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误引起的异常
     */
    @Override
    public void importMaterialList(MultipartFile file) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        List<Material> materialList = new ArrayList<>(128);
        List<String> codeList = materialRepository.findAllMaterialCode();
        List<String> targetCodeList = new ArrayList<>();

        // Workbook行索引
        int index = 0;
        String structureNo = null;
        for (Row row : sheet) {
            // 前三行数据为机器信息及字段的批注，所以不予解析
            if (index < 3) {
                index++;
            } else {
                if (null == row.getCell(3) || "".equals(row.getCell(3).toString().trim())) {
                    continue;
                }
                Material material = Material.newInstance();
                String code = row.getCell(3).toString().trim();
                if (!codeList.contains(code) && !targetCodeList.contains(code)) {
                    material.setCode(code);
                    material.setDrawingNo(code);
                    if (null != row.getCell(0) && !"".equals(row.getCell(0).toString().trim())) {
                        structureNo = row.getCell(0).toString().trim();
                    }
                    material.setStructureNo(structureNo);
                    if (null != row.getCell(7)) {
                        material.setName(row.getCell(7).toString().trim());
                    }
                    if (null != row.getCell(8)) {
                        material.setSpecification(row.getCell(8).toString().trim());
                    }
                    if (null != row.getCell(9)) {
                        material.setModel(row.getCell(9).toString().trim());
                    }
                    if (null != row.getCell(17)) {
                        material.setDescription(row.getCell(17).toString().trim());
                    }
                    if (code.startsWith("EN")) {
                        material.setQualifiedMark("Y");
                        material.setBatchMark("N");
                    } else {
                        material.setQualifiedMark("N");
                        material.setBatchMark("Y");
                    }
                    String source = "";
                    if (null != row.getCell(12)) {
                        source = row.getCell(12).toString().trim();
                    }
                    material.setResourceMark(source);
                    /*
                     * 检查货源是否为*
                     * 以B P K开头的，都是采购件，
                     * 货源是B P5 P6的都是合批的，其他的采购件均不合批
                     * 以K开头的都是集采，其他的都是自采
                     */
                    if (!"*".equals(source)) {
                        if (source.startsWith("B") || source.startsWith("P") || source.startsWith("K")) {
                            if ("B".equals(source) || "P5".equals(source) || "P6".equals(source)) {
                                material.setQualifiedMark("Y");
                            } else {
                                material.setQualifiedMark("N");
                            }
                            if (source.startsWith("K")) {
                                material.setGroupPurMark("Y");
                                material.setOwnPurMark("N");
                            } else {
                                material.setGroupPurMark("N");
                                material.setOwnPurMark("Y");
                            }
                            material.setSourceMark("P");
                            material.setPurchaseMark("Y");
                            material.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
                        } else if (source.startsWith("Z")) {
                            // 货源标识以Z开始的，是自制，不合批，不采购，不集采，不自采，无采购分类
                            material.setQualifiedMark("N");
                            material.setSourceMark("M");
                            material.setPurchaseMark("N");
                            material.setGroupPurMark("N");
                            material.setOwnPurMark("N");
                            material.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
                        }
                    }
                    material.setRespCompany("03");
                    material.setRespDept("6202");
                    material.setInventoryUnit("025");
                    material.setKeyPartMark("Y");
                    material.setInspectMark("Y");
                    materialList.add(material);
                    targetCodeList.add(material.getCode());
                }
            }
        }

        materialRepository.saveAll(materialList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbook exportMaterialList() {
        List<Material> materialList = materialRepository.findAllByExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
        Workbook workbook = createWorkbook(materialList);
        for (Material material : materialList) {
            material.setExportStatus(Constant.Material.ExportStatus.EXPORTED);
        }
        materialRepository.saveAll(materialList);
        return workbook;
    }


    private void checkExportStatus(Material material) {
        if (Constant.Material.PerfectStatus.PERFECTED == material.getTechnologyStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getQualityStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getPurchaseStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getAssemblyStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getProduceStatus()) {
            material.setExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialTechnology(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setName(material.getName());
        targetMater.setSpecification(material.getSpecification());
        targetMater.setModel(material.getModel());
        targetMater.setDescription(material.getDescription());
        targetMater.setDrawingNo(material.getDrawingNo());
        targetMater.setGeneralSort(material.getGeneralSort());
        targetMater.setInventoryUnit(material.getInventoryUnit());
        if (null == targetMater.getSourceMark()) {
            if ("M".equals(material.getSourceMark())) {
                targetMater.setPurchaseMark("N");
                targetMater.setGroupPurMark("N");
                targetMater.setOwnPurMark("N");
                targetMater.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
            } else if ("P".equals(material.getSourceMark())) {
                targetMater.setPurchaseMark("Y");
                targetMater.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
            }
        } else {
            if (!targetMater.getSourceMark().equals(material.getSourceMark())) {
                // 当采购自制件标记不为空时，说明该物料已经被技术中心标记过
                // 这种情况下物料中就可能有相应部门修改的数据，所以应该把这些数据清空
                if ("P".equals(targetMater.getSourceMark()) && "M".equals(material.getSourceMark())) {
                    targetMater.setPurchaseSort(null);
                    targetMater.setPurchaseMark("N");
                    targetMater.setGroupPurMark("N");
                    targetMater.setOwnPurMark("N");
                    targetMater.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
                    targetMater.setProduceStatus(Constant.Material.PerfectStatus.IMPERFECT);
                } else if ("M".equals(targetMater.getSourceMark()) && "P".equals(material.getSourceMark())) {
                    targetMater.setOutSource("N");
                    targetMater.setPlanner("QD624");
                    targetMater.setPurchaseMark("Y");
                    targetMater.setGroupPurMark(null);
                    targetMater.setOwnPurMark(null);
                    targetMater.setPurchaseStatus(Constant.Material.PerfectStatus.IMPERFECT);
                    targetMater.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
                }
                if (Constant.Material.ExportStatus.EXPORTABLE == targetMater.getExportStatus()) {
                    targetMater.setExportStatus(Constant.Material.ExportStatus.IMPERFECT);
                }
                targetMater.setFixedAdvTime(null);
            }
        }
        targetMater.setSourceMark(material.getSourceMark());
        targetMater.setRespCompany(material.getRespCompany());
        targetMater.setRespDept(material.getRespDept());
        targetMater.setKeyPartMark(material.getKeyPartMark());
        targetMater.setKeyPartSort(material.getKeyPartSort());
        targetMater.setVirtualPartMark(material.getVirtualPartMark());
        targetMater.setQualifiedMark(material.getQualifiedMark());
        targetMater.setTechnologyStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialQuality(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setInspectMark(material.getInspectMark());
        targetMater.setBatchMark(material.getBatchMark());
        targetMater.setQualityStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialPurchase(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setPurchaseSort(material.getPurchaseSort());
        targetMater.setPurchaseMark(material.getPurchaseMark());
        targetMater.setGroupPurMark(material.getGroupPurMark());
        targetMater.setOwnPurMark(material.getOwnPurMark());
        targetMater.setFixedAdvTime(material.getFixedAdvTime());
        targetMater.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialAssembly(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setDefRepository(material.getDefRepository());
        targetMater.setAssemblyStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialProduce(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setOutSource(material.getOutSource());
        targetMater.setPlanner(material.getPlanner());
        targetMater.setFixedAdvTime(material.getFixedAdvTime());
        targetMater.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Material findMaterialByCode(String code) {
        return materialRepository.findMaterialByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PageContext<Material> findMaterialListByPagination(int pageIndex, int pageSize, int exportStatus, int perfectStatus, User user) {
        // 指定排序参数对象：根据id，进行升序查询
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        /*
         * 封装分页实体 Pageable
         * 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页)
         * 参数二：表示每页展示多少数据，现在设置每页展示100条数据
         * 参数三：封装排序对象，根据该对象的参数指定根据id升序查询
         * */
        Page<Material> materialPage = null;

        if (user.getRoles().getOrDefault(Constant.UserRoles.TECHNOLOGY_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (3 != exportStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.EXPORT_STATUS), exportStatus));
                }
                if (2 != perfectStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.TECHNOLOGY_STATUS), perfectStatus));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }

        if (user.getRoles().getOrDefault(Constant.UserRoles.QUALITY_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (2 != perfectStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.QUALITY_STATUS), perfectStatus));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }

        if (user.getRoles().getOrDefault(Constant.UserRoles.PURCHASE_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.TECHNOLOGY_STATUS), Constant.Material.PerfectStatus.PERFECTED));
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.SOURCE_MARK), "P"));
                if (2 != perfectStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.PURCHASE_STATUS), perfectStatus));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }

        if (user.getRoles().getOrDefault(Constant.UserRoles.ASSEMBLY_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (2 != perfectStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.ASSEMBLY_STATUS), perfectStatus));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }

        if (user.getRoles().getOrDefault(Constant.UserRoles.PRODUCE_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.TECHNOLOGY_STATUS), Constant.Material.PerfectStatus.PERFECTED));
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.SOURCE_MARK), "M"));
                if (2 != perfectStatus) {
                    predicates.add(criteriaBuilder.equal(root.get(Constant.Material.PRODUCE_STATUS), perfectStatus));
                }
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }

        PageContext<Material> pageContext = new PageContext<>();
        pageContext.setIndex(pageIndex);
        if (materialPage != null) {
            pageContext.setDataTotal(materialPage.getTotalElements());
            pageContext.setPageTotal(materialPage.getTotalPages());
            pageContext.setData(materialPage.getContent());
        }
        return pageContext;
    }

    private Workbook createWorkbook(List<Material> materialList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        int rowIndex = 0;

        XSSFRow row0 = sheet.createRow(rowIndex);
        XSSFCell cell01 = row0.createCell(0);
        cell01.setCellValue("公司号");
        XSSFCell cell02 = row0.createCell(1);
        cell02.setCellValue("物料编码");
        XSSFCell cell03 = row0.createCell(2);
        cell03.setCellValue("物料名称");
        XSSFCell cell04 = row0.createCell(3);
        cell04.setCellValue("物料简称");
        XSSFCell cell05 = row0.createCell(4);
        cell05.setCellValue("规格");
        XSSFCell cell06 = row0.createCell(5);
        cell06.setCellValue("型号");
        XSSFCell cell07 = row0.createCell(6);
        cell07.setCellValue("具体描述");
        XSSFCell cell08 = row0.createCell(7);
        cell08.setCellValue("国标号");
        XSSFCell cell09 = row0.createCell(8);
        cell09.setCellValue("图号");
        XSSFCell cell010 = row0.createCell(9);
        cell010.setCellValue("参考号");
        XSSFCell cell011 = row0.createCell(10);
        cell011.setCellValue("普通分类");
        XSSFCell cell012 = row0.createCell(11);
        cell012.setCellValue("库存单位");
        XSSFCell cell013 = row0.createCell(12);
        cell013.setCellValue("采购自制件标记");
        XSSFCell cell014 = row0.createCell(13);
        cell014.setCellValue("责任部门");
        XSSFCell cell015 = row0.createCell(14);
        cell015.setCellValue("责任公司");
        XSSFCell cell016 = row0.createCell(15);
        cell016.setCellValue("关键件标记");
        XSSFCell cell017 = row0.createCell(16);
        cell017.setCellValue("关键件大类");
        XSSFCell cell018 = row0.createCell(17);
        cell018.setCellValue("虚拟标记");
        XSSFCell cell019 = row0.createCell(18);
        cell019.setCellValue("合批标记");
        XSSFCell cell020 = row0.createCell(19);
        cell020.setCellValue("质检标记");
        XSSFCell cell021 = row0.createCell(20);
        cell021.setCellValue("批次标记");
        XSSFCell cell022 = row0.createCell(21);
        cell022.setCellValue("采购分类");
        XSSFCell cell023 = row0.createCell(22);
        cell023.setCellValue("可采购标记");
        XSSFCell cell024 = row0.createCell(23);
        cell024.setCellValue("可集采标记");
        XSSFCell cell025 = row0.createCell(24);
        cell025.setCellValue("可自采标记");
        XSSFCell cell026 = row0.createCell(25);
        cell026.setCellValue("默认仓库");
        XSSFCell cell027 = row0.createCell(26);
        cell027.setCellValue("外协标记");
        XSSFCell cell028 = row0.createCell(27);
        cell028.setCellValue("计划员");
        XSSFCell cell029 = row0.createCell(28);
        cell029.setCellValue("固定提前期");

        for (com.cse.naruto.model.Material material : materialList) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(material.getCompanyNo());
            XSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(material.getCode());
            XSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(material.getName());
            XSSFCell cell4 = row.createCell(3);
            cell4.setCellValue(material.getName());
            XSSFCell cell5 = row.createCell(4);
            cell5.setCellValue(material.getSpecification());
            XSSFCell cell6 = row.createCell(5);
            cell6.setCellValue(material.getModel());
            XSSFCell cell7 = row.createCell(6);
            cell7.setCellValue(material.getDescription());
            XSSFCell cell8 = row.createCell(7);
            cell8.setCellValue(material.getInternational());
            XSSFCell cell9 = row.createCell(8);
            cell9.setCellValue(material.getDrawingNo());
            XSSFCell cell10 = row.createCell(9);
            cell10.setCellValue(material.getReference());
            XSSFCell cell11 = row.createCell(10);
            cell11.setCellValue(material.getGeneralSort());
            XSSFCell cell12 = row.createCell(11);
            cell12.setCellValue(material.getInventoryUnit());
            XSSFCell cell13 = row.createCell(12);
            cell13.setCellValue(material.getSourceMark());
            XSSFCell cell14 = row.createCell(13);
            cell14.setCellValue(material.getRespDept());
            XSSFCell cell15 = row.createCell(14);
            cell15.setCellValue(material.getRespCompany());
            XSSFCell cell16 = row.createCell(15);
            cell16.setCellValue(material.getKeyPartMark());
            XSSFCell cell17 = row.createCell(16);
            cell17.setCellValue(material.getKeyPartSort());
            XSSFCell cell18 = row.createCell(17);
            cell18.setCellValue(material.getVirtualPartMark());
            XSSFCell cell19 = row.createCell(18);
            cell19.setCellValue(material.getQualifiedMark());
            XSSFCell cell20 = row.createCell(19);
            cell20.setCellValue(material.getInspectMark());
            XSSFCell cell21 = row.createCell(20);
            cell21.setCellValue(material.getBatchMark());
            XSSFCell cell22 = row.createCell(21);
            cell22.setCellValue(material.getPurchaseSort());
            XSSFCell cell23 = row.createCell(22);
            cell23.setCellValue(material.getPurchaseMark());
            XSSFCell cell24 = row.createCell(23);
            cell24.setCellValue(material.getGroupPurMark());
            XSSFCell cell25 = row.createCell(24);
            cell25.setCellValue(material.getOwnPurMark());
            XSSFCell cell26 = row.createCell(25);
            cell26.setCellValue(material.getDefRepository());
            XSSFCell cell27 = row.createCell(26);
            cell27.setCellValue(material.getOutSource());
            XSSFCell cell28 = row.createCell(27);
            // 因为自制件计划员有默认值，当该物料为非采购件时，不能输入默认值，以下列必须为空
            if ("M".equals(material.getSourceMark())) {
                cell28.setCellValue(material.getPlanner());
            }
            XSSFCell cell29 = row.createCell(28);
            cell29.setCellValue(material.getFixedAdvTime());
        }

        return workbook;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Material> findMaterialListByCodeLike(String queryStr, Integer perfectStatus, User user) {
        String str = queryStr + "%";
        if (2 != perfectStatus && user.getRoles().getOrDefault(Constant.UserRoles.TECHNOLOGY_EMPLOYEE, false)) {
            return materialRepository.findAllByCodeLikeAndTechnologyStatus(str, perfectStatus);
        } else {
            return materialRepository.findAllByCodeLike(str);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Material> findMaterialListByStructureNo(String structureNo, Integer perfectStatus, User user) {
        if (2 != perfectStatus && user.getRoles().getOrDefault(Constant.UserRoles.TECHNOLOGY_EMPLOYEE, false)) {
            return materialRepository.findAllByStructureNoAndTechnologyStatus(structureNo, perfectStatus);
        } else {
            return materialRepository.findAllByStructureNo(structureNo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<Material> findMaterialListByResourceMark(String resourceMark, Integer perfectStatus, User user) {
        if (2 != perfectStatus && user.getRoles().getOrDefault(Constant.UserRoles.TECHNOLOGY_EMPLOYEE, false)) {
            return materialRepository.findAllByResourceMarkAndTechnologyStatus(resourceMark, perfectStatus);
        } else {
            return materialRepository.findAllByResourceMark(resourceMark);
        }
    }
}
