package com.cse.naruto.service.impl;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.PageContext;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.service.MaterialService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料业务逻辑
 *
 * @author 王振琦
 * createAt 2018/10/16
 * updateAt 2018/10/17
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    private Material getNewMaterial() {
        Material material = new Material();
        material.setObjectId(Generator.getObjectId());
        material.setStatus(Constant.MaterialStatus.IMPERFECT);
        LocalDateTime dateTime = LocalDateTime.now();
        material.setCreateAt(dateTime);
        material.setUpdateAt(dateTime);
        material.setCompanyNo("01");
        material.setVirtualPartMark("N");
        material.setOutSource("N");
        material.setTechnologyDeptMark(false);
        material.setQualifiedDeptMark(false);
        material.setPurchaseDeptMark(false);
        material.setAssemblyDeptMark(false);
        material.setOperateDeptMark(false);
        return material;
    }

    @Override
    public void saveMaterialListByBOM(MultipartFile file) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        List<Material> materialList = new ArrayList<>(128);
        List<String> codeList = materialRepository.findAllMaterialCode();
        List<String> targetCodeList = new ArrayList<>();

        // Workbook行索引
        int index = 0;
        for (Row row : sheet) {
            // 前三行数据为机器信息及字段的批注，所以不予解析
            if (index < 3) {
                index++;
            } else {
                if (null == row.getCell(3)) {
                    break;
                }
                Material material = getNewMaterial();
                String code = row.getCell(3).toString();
                if (!codeList.contains(code) && !targetCodeList.contains(code)) {
                    material.setCode(code);
                    if (null != row.getCell(7)) {
                        material.setName(row.getCell(7).toString());
                    }
                    material.setShortName("");
                    materialList.add(material);
                    targetCodeList.add(material.getCode());
                }
            }
        }

        materialRepository.saveAll(materialList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbook findMaterialListToExcel() {
        List<Material> materialList = materialRepository.findAllByStatus(Constant.MaterialStatus.PERFECTED_AND_CAN_EXPORT);
        Workbook workbook = createWorkbook(materialList);
        for (Material material : materialList) {
            material.setStatus(Constant.MaterialStatus.EXPORTED);
        }
        materialRepository.saveAll(materialList);
        return workbook;
    }


    private void statusInspect(Material material) {
        if (material.getTechnologyDeptMark()
                && material.getQualifiedDeptMark()
                && material.getPurchaseDeptMark()
                && material.getAssemblyDeptMark()
                && material.getOperateDeptMark()) {
            material.setStatus(Constant.MaterialStatus.PERFECTED_AND_CAN_EXPORT);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByTechnologyCenter(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setSpecification(material.getSpecification());
        targetMater.setModel(material.getModel());
        targetMater.setDescription(material.getDescription());
        targetMater.setDrawingNo(material.getDrawingNo());
        targetMater.setGeneralSort(material.getGeneralSort());
        targetMater.setInventoryUnit(material.getInventoryUnit());
        targetMater.setSourceMark(material.getSourceMark());
        targetMater.setRespCompany(material.getRespCompany());
        targetMater.setRespDept(material.getRespDept());
        targetMater.setKeyPartMark(material.getKeyPartMark());
        targetMater.setKeyPartSort(material.getKeyPartSort());
        targetMater.setVirtualPartMark(material.getVirtualPartMark());
        targetMater.setQualifiedMark(material.getQualifiedMark());
        targetMater.setTechnologyDeptMark(true);
        targetMater.setUpdateAt(LocalDateTime.now());
        statusInspect(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByQualifiedEnvironment(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setInspectMark(material.getInspectMark());
        targetMater.setBatchMark(material.getBatchMark());
        targetMater.setQualifiedDeptMark(true);
        targetMater.setUpdateAt(LocalDateTime.now());
        statusInspect(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByPurchase(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setPurchaseSort(material.getPurchaseSort());
        targetMater.setPurchaseMark(material.getPurchaseMark());
        targetMater.setGroupPurMark(material.getGroupPurMark());
        targetMater.setOwnPurMark(material.getOwnPurMark());
        targetMater.setPurchaseDeptMark(true);
        targetMater.setUpdateAt(LocalDateTime.now());
        statusInspect(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByAssemblyCenter(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setDefRepository(material.getDefRepository());
        targetMater.setAssemblyDeptMark(true);
        targetMater.setUpdateAt(LocalDateTime.now());
        statusInspect(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialByProduceOperate(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setOutSource(material.getOutSource());
        targetMater.setPlanner(material.getPlanner());
        targetMater.setFixedAdvTime(material.getFixedAdvTime());
        targetMater.setOperateDeptMark(true);
        targetMater.setUpdateAt(LocalDateTime.now());
        statusInspect(targetMater);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Material findMaterialByCode(String code) {
        return materialRepository.findMaterialByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PageContext<Material> findMaterialListByPagination(int pageIndex, int pageSize, int status) {
        // 指定排序参数对象：根据id，进行降序查询
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        /**
         * 封装分页实体 Pageable
         * 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页)
         * 参数二：表示每页展示多少数据，现在设置每页展示2条数据
         * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
         * */
        Page<Material> materialData;
        if (0 == status) {
            materialData = materialRepository.findAll(
                    (Specification<Material>) (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.between(root.get("status"), 1, 2),
                    PageRequest.of(pageIndex - 1, pageSize, sort));
        } else {
            materialData = materialRepository.findAll(
                    (Specification<Material>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status),
                    PageRequest.of(pageIndex - 1, pageSize, sort));
        }
        PageContext<Material> pageContext = new PageContext<>();
        pageContext.setIndex(pageIndex);
        pageContext.setDataTotal(materialData.getTotalElements());
        pageContext.setPageTotal(materialData.getTotalPages());
        pageContext.setData(materialData.getContent());
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
        cell08.setCellValue("图号");
        XSSFCell cell09 = row0.createCell(8);
        cell09.setCellValue("普通分类");
        XSSFCell cell010 = row0.createCell(9);
        cell010.setCellValue("库存单位");
        XSSFCell cell011 = row0.createCell(10);
        cell011.setCellValue("采购自制件标记");
        XSSFCell cell012 = row0.createCell(11);
        cell012.setCellValue("责任部门");
        XSSFCell cell013 = row0.createCell(12);
        cell013.setCellValue("责任公司");
        XSSFCell cell014 = row0.createCell(13);
        cell014.setCellValue("关键件标记");
        XSSFCell cell015 = row0.createCell(14);
        cell015.setCellValue("关键件大类");
        XSSFCell cell016 = row0.createCell(15);
        cell016.setCellValue("虚拟标记");
        XSSFCell cell017 = row0.createCell(16);
        cell017.setCellValue("合批标记");
        XSSFCell cell018 = row0.createCell(17);
        cell018.setCellValue("质检标记");
        XSSFCell cell019 = row0.createCell(18);
        cell019.setCellValue("批次标记");
        XSSFCell cell020 = row0.createCell(19);
        cell020.setCellValue("采购分类");
        XSSFCell cell021 = row0.createCell(20);
        cell021.setCellValue("可采购标记");
        XSSFCell cell022 = row0.createCell(21);
        cell022.setCellValue("可集采标记");
        XSSFCell cell023 = row0.createCell(22);
        cell023.setCellValue("可自采标记");
        XSSFCell cell024 = row0.createCell(23);
        cell024.setCellValue("默认仓库");
        XSSFCell cell025 = row0.createCell(24);
        cell025.setCellValue("外协标记");
        XSSFCell cell026 = row0.createCell(25);
        cell026.setCellValue("计划员");
        XSSFCell cell027 = row0.createCell(26);
        cell027.setCellValue("固定提前期");

        for (Material material : materialList) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(material.getCompanyNo());
            XSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(material.getCode());
            XSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(material.getName());
            XSSFCell cell4 = row.createCell(3);
            cell4.setCellValue(material.getShortName());
            XSSFCell cell5 = row.createCell(4);
            cell5.setCellValue(material.getSpecification());
            XSSFCell cell6 = row.createCell(5);
            cell6.setCellValue(material.getModel());
            XSSFCell cell7 = row.createCell(6);
            cell7.setCellValue(material.getDescription());
            XSSFCell cell8 = row.createCell(7);
            cell8.setCellValue(material.getDrawingNo());
            XSSFCell cell9 = row.createCell(8);
            cell9.setCellValue(material.getGeneralSort());
            XSSFCell cell10 = row.createCell(9);
            cell10.setCellValue(material.getInventoryUnit());
            XSSFCell cell11 = row.createCell(10);
            cell11.setCellValue(material.getSourceMark());
            XSSFCell cell12 = row.createCell(11);
            cell12.setCellValue(material.getRespDept());
            XSSFCell cell13 = row.createCell(12);
            cell13.setCellValue(material.getRespCompany());
            XSSFCell cell14 = row.createCell(13);
            cell14.setCellValue(material.getKeyPartMark());
            XSSFCell cell15 = row.createCell(14);
            cell15.setCellValue(material.getKeyPartSort());
            XSSFCell cell16 = row.createCell(15);
            cell16.setCellValue(material.getVirtualPartMark());
            XSSFCell cell17 = row.createCell(16);
            cell17.setCellValue(material.getQualifiedMark());
            XSSFCell cell18 = row.createCell(17);
            cell18.setCellValue(material.getInspectMark());
            XSSFCell cell19 = row.createCell(18);
            cell19.setCellValue(material.getBatchMark());
            XSSFCell cell20 = row.createCell(19);
            cell20.setCellValue(material.getPurchaseSort());
            XSSFCell cell21 = row.createCell(20);
            cell21.setCellValue(material.getPurchaseMark());
            XSSFCell cell22 = row.createCell(21);
            cell22.setCellValue(material.getGroupPurMark());
            XSSFCell cell23 = row.createCell(22);
            cell23.setCellValue(material.getOwnPurMark());
            XSSFCell cell24 = row.createCell(23);
            cell24.setCellValue(material.getDefRepository());
            XSSFCell cell25 = row.createCell(24);
            cell25.setCellValue(material.getOutSource());
            XSSFCell cell26 = row.createCell(25);
            cell26.setCellValue(material.getPlanner());
            XSSFCell cell27 = row.createCell(26);
            cell27.setCellValue(material.getFixedAdvTime());
        }

        return workbook;
    }
}
