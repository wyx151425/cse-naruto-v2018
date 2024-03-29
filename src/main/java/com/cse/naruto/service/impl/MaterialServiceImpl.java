package com.cse.naruto.service.impl;

import com.cse.naruto.model.*;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.repository.StatisticRepository;
import com.cse.naruto.service.MaterialService;
import com.cse.naruto.util.Constant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
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
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final StatisticRepository statisticRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, StatisticRepository statisticRepository) {
        this.materialRepository = materialRepository;
        this.statisticRepository = statisticRepository;
    }

    /**
     * 将Excel的每一个单元格格式化为文本格式
     *
     * @param file      文件
     * @param sheetName 工作簿名称
     * @return 工作簿
     * @throws InvalidFormatException 文件格式错误
     * @throws IOException            输入输出异常
     */
    private Sheet formatExcelBOM(MultipartFile file, String sheetName) throws InvalidFormatException, IOException {
        // 获取工作簿
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        // 获取BOM的Sheet
        Sheet sheet = workbook.getSheet(sheetName);
        // 创建统一风格，格式化每一个单元格
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellStyle(cellStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            }
        }
        return sheet;
    }

    /**
     * 通过BOM导入物料数据列表
     *
     * @param file 前端提交的柴油机BOM
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误引起的异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportResult> importMaterialList(MultipartFile file, User user) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        List<Material> materialList = new ArrayList<>(128);
        List<String> codeList = materialRepository.findAllMaterialCode();
        List<String> targetCodeList = new ArrayList<>();
        List<ImportResult> resultList = new ArrayList<>();
        Statistic statistic = Statistic.newInstance();

        // Workbook行索引
        int index = 0;
        for (Row row : sheet) {
            index++;
            // 前三行数据为机器信息及字段的批注，所以不予解析
            if (index > 3) {
                if (null == row.getCell(3) || "".equals(row.getCell(3).toString().trim())) {
                    resultList.add(new ImportResult(index, "未填写物料号"));
                    continue;
                }
                if (null == row.getCell(7) || "".equals(row.getCell(7).toString().trim())) {
                    resultList.add(new ImportResult(index, "未填写物料名称"));
                    continue;
                }
                if (null == row.getCell(12) || "".equals(row.getCell(12).toString().trim())) {
                    resultList.add(new ImportResult(index, "未填写货源"));
                    continue;
                }
                if (null == row.getCell(17) || "".equals(row.getCell(17).toString().trim())) {
                    resultList.add(new ImportResult(index, "未填写喷涂防护"));
                    continue;
                }
                Material material = Material.newInstance();
                String code = row.getCell(3).toString().trim();
                if (!codeList.contains(code) && !targetCodeList.contains(code)) {
                    material.setCode(code);
                    material.setDrawingNo(code);
                    if (null != row.getCell(0) && !"".equals(row.getCell(0).toString().trim())) {
                        material.setStructureNo(row.getCell(0).toString().trim());
                    }
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
//                    if (code.startsWith("EN") || code.startsWith("01") || code.startsWith("02")) {
//                        material.setBatchMark("N");
//                    } else {
                    // 2019/12/10更新 物料全部改为批次管理
                    material.setBatchMark("Y");
//                    }
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

                            material.setSourceMark("P");
                            material.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
                        } else if (source.startsWith("Z")) {
                            // 货源标识以Z开始的，是自制，不合批，不采购，不集采，不自采，无采购分类
                            material.setSourceMark("M");
                            material.setGroupPurMark("N");
                            material.setOwnPurMark("N");
                            material.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
                        }

                        if (source.startsWith("B") || source.startsWith("F")) {
                            material.setQualifiedMark("Y");
                        } else {
                            material.setQualifiedMark("N");
                        }

                        if (source.startsWith("K")) {
                            material.setGroupPurMark("Y");
                            material.setOwnPurMark("N");
                            material.setRespCompany("01");
                            material.setRespDept("6004");
                        } else {
                            material.setGroupPurMark("N");
                            material.setOwnPurMark("Y");
                            material.setRespCompany("03");
                            material.setRespDept("6202");
                        }
                    }

                    material.setInventoryUnit("025");
                    material.setKeyPartMark("Y");
                    material.setInspectMark("Y");
                    material.setPurchaseMark("Y");
                    material.setPurchaseSort("P01");
                    material.setPrincipal(user.getCode());
                    material.setQualityStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setToken(statistic.getToken());
                    materialList.add(material);
                    targetCodeList.add(material.getCode());
                }
            }
        }

        statisticRepository.save(statistic);
        materialRepository.saveAll(materialList);
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importGuaranteeMaterialList(MultipartFile file, User user) throws IOException, InvalidFormatException {
        Sheet sheet = formatExcelBOM(file, "sBasicData");
        List<Material> materialList = new ArrayList<>(100);
        List<String> codeList = materialRepository.findAllMaterialCode();
        Statistic statistic = Statistic.newInstance();

        int rowIndex = 0;
        for (Row row : sheet) {
            if (rowIndex < 1) {
                rowIndex++;
            } else {
                if (null == row.getCell(0) || "".equals(row.getCell(0).toString().trim())) {
                    continue;
                }
                Material material;
                String code = row.getCell(0).toString().trim();
                if (!codeList.contains(code)) {
                    material = Material.newInstance();
                    material.setCode(code);
                    material.setDrawingNo(row.getCell(1).toString().trim());
                    material.setName(row.getCell(2).toString().trim());
                    if (null != row.getCell(3) && !"".equals(row.getCell(3).toString().trim())) {
                        material.setSpecification(row.getCell(3).toString().trim());
                    }
                    if (null != row.getCell(4) && !"".equals(row.getCell(4).toString().trim())) {
                        material.setModel(row.getCell(4).toString().trim());
                    }
                    if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                        material.setDescription(row.getCell(5).toString().trim());
                    }
                    material.setStructureNo("SG-9999");
                    material.setGeneralSort("G15");
                    material.setInspectMark("Y");
                    material.setBatchMark("Y");
                    material.setInventoryUnit("025");
                    material.setDefRepository("60");
                    material.setVirtualPartMark("N");
                    material.setSourceMark("P");
                    material.setOutSource("N");
                    material.setRespDept("6202");
                    material.setRespCompany("03");
                    material.setKeyPartSort("Y4");
                    material.setKeyPartMark("Y");
                    material.setPurchaseSort("P01");
                    material.setFixedAdvTime("90");
                    material.setQualifiedMark("N");
                    material.setPurchaseMark("Y");
                    material.setGroupPurMark("N");
                    material.setOwnPurMark("Y");
                    material.setResourceMark("P");
                    material.setTechnologyStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setQualityStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setAssemblyStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
                    material.setExportStatus(Constant.Material.ExportStatus.IMPERFECT);
                    material.setToken(statistic.getToken());
                    materialList.add(material);
                }
            }
        }
        materialRepository.saveAll(materialList);
        statisticRepository.save(statistic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportResult> importPBOM(MultipartFile file, User user) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        List<Material> materialList = new ArrayList<>(128);
        List<String> codeList = materialRepository.findAllMaterialCode();
        List<String> targetCodeList = new ArrayList<>();

        List<ImportResult> resultList = new ArrayList<>();

        // Workbook行索引
        int index = 0;
        for (Row row : sheet) {
            index++;
            // 前三行数据为机器信息及字段的批注，所以不予解析
            if (index > 3) {
                if (null == row.getCell(3) || "".equals(row.getCell(3).toString().trim())) {
                    continue;
                }
                Material material = Material.newInstance();
                String code = row.getCell(3).toString().trim();
                if (!codeList.contains(code) && !targetCodeList.contains(code)) {
                    material.setCode(code);
                    material.setDrawingNo(code);
                    if (null != row.getCell(0) && !"".equals(row.getCell(0).toString().trim())) {
                        material.setStructureNo(row.getCell(0).toString().trim());
                    }
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
//                    if (code.startsWith("EN") || code.startsWith("01") || code.startsWith("02")) {
//                        material.setBatchMark("N");
//                    } else {
                    // 2019/12/10更新 全部改为批次管理
                    material.setBatchMark("Y");
//                    }
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

                            material.setSourceMark("P");
                            material.setProduceStatus(Constant.Material.PerfectStatus.PERFECTED);
                        } else if (source.startsWith("Z")) {
                            // 货源标识以Z开始的，是自制，不合批，不采购，不集采，不自采，无采购分类
                            material.setSourceMark("M");
                            material.setGroupPurMark("N");
                            material.setOwnPurMark("Y");
                            material.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
                        }

                        if (source.startsWith("B") || source.startsWith("F")) {
                            material.setQualifiedMark("Y");
                        } else {
                            material.setQualifiedMark("N");
                        }

                        if (source.startsWith("K")) {
                            material.setGroupPurMark("Y");
                            material.setOwnPurMark("N");
                            material.setRespCompany("01");
                            material.setRespDept("6004");
                        } else {
                            material.setGroupPurMark("N");
                            material.setOwnPurMark("Y");
                            material.setRespCompany("03");
                            material.setRespDept("6202");
                        }
                    }


                    material.setInventoryUnit("025");
                    material.setKeyPartMark("Y");
                    material.setInspectMark("Y");
                    material.setPurchaseMark("Y");
                    material.setPurchaseSort("P01");
                    material.setPrincipal(user.getCode());
                    material.setQualityStatus(Constant.Material.PerfectStatus.PERFECTED);


                    boolean flag = true;
                    String result = " 导入成功，信息已完善";
                    if (null != row.getCell(20) && !"".equals(row.getCell(20).toString().trim())) {
                        material.setResourceMark(row.getCell(20).toString().trim());
                    }
                    if (null != row.getCell(21) && !"".equals(row.getCell(21).toString().trim())) {
                        material.setDescription(row.getCell(21).toString().trim());
                    }
                    if (null != row.getCell(22) && !"".equals(row.getCell(22).toString().trim())) {
                        material.setInventoryUnit(row.getCell(22).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(23) && !"".equals(row.getCell(23).toString().trim())) {
                        material.setSourceMark(row.getCell(23).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(24) && !"".equals(row.getCell(24).toString().trim())) {
                        material.setRespDept(row.getCell(24).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(25) && !"".equals(row.getCell(25).toString().trim())) {
                        material.setGeneralSort(row.getCell(25).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(26) && !"".equals(row.getCell(26).toString().trim())) {
                        material.setKeyPartSort(row.getCell(26).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(27) && !"".equals(row.getCell(27).toString().trim())) {
                        material.setQualifiedMark(row.getCell(27).toString().trim());
                    } else {
                        flag = false;
                    }
                    if (null != row.getCell(28) && !"".equals(row.getCell(28).toString().trim())) {
                        material.setSpecification(row.getCell(28).toString().trim());
                    }
                    if (null != row.getCell(29) && !"".equals(row.getCell(29).toString().trim())) {
                        material.setModel(row.getCell(29).toString().trim());
                    }
                    if (null != row.getCell(30) && !"".equals(row.getCell(30).toString().trim())) {
                        material.setRespCompany(row.getCell(30).toString().trim());
                    } else {
                        flag = false;
                    }

                    if (flag) {
                        material.setTechnologyStatus(Constant.Material.PerfectStatus.PERFECTED);
                        checkExportStatus(material, 1);
                    } else {
                        result = " 导入成功，信息未完善";
                    }

                    result = code + result;

                    resultList.add(new ImportResult(index, result));

                    materialList.add(material);
                    targetCodeList.add(material.getCode());

                }
            }
        }

        materialRepository.saveAll(materialList);
        return resultList;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XSSFWorkbook perfectPBOM(MultipartFile file) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("整机BOM");
        int index = 0;
        for (Row row : sheet) {
            if (0 == index) {
                row.createCell(20).setCellValue("货源");
                row.createCell(21).setCellValue("具体描述（喷涂）");
                row.createCell(22).setCellValue("库存单位");
                row.createCell(23).setCellValue("采购自制标记");
                row.createCell(24).setCellValue("责任部门");
                row.createCell(25).setCellValue("普通分类");
                row.createCell(26).setCellValue("关键件大类");
                row.createCell(27).setCellValue("合批标记");
                row.createCell(28).setCellValue("规格");
                row.createCell(29).setCellValue("型号");
                row.createCell(30).setCellValue("责任公司");
            }
            if (index < 3) {
                index++;
            } else {
                String code = "";
                if (null != row.getCell(3)) {
                    code = row.getCell(3).toString().trim();
                }
                Material material = materialRepository.findMaterialByCode(code);
                if (null != material) {
                    if (null != material.getResourceMark()) {
                        row.createCell(20).setCellValue(material.getResourceMark());
                    }
                    row.createCell(21).setCellValue(material.getDescription());
                    row.createCell(22).setCellValue(material.getInventoryUnit());
                    row.createCell(23).setCellValue(material.getSourceMark());
                    row.createCell(24).setCellValue(material.getRespDept());
                    row.createCell(25).setCellValue(material.getGeneralSort());
                    row.createCell(26).setCellValue(material.getKeyPartSort());
                    row.createCell(27).setCellValue(material.getQualifiedMark());
                    row.createCell(28).setCellValue(material.getSpecification());
                    row.createCell(29).setCellValue(material.getModel());
                    row.createCell(30).setCellValue(material.getRespCompany());
                }
            }
        }
        return workbook;
    }

    /**
     * 通过BOM导入物料数据列表
     *
     * @param file 前端提交的柴油机BOM
     * @throws IOException            输入输出异常
     * @throws InvalidFormatException 文件格式错误引起的异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importOriginMaterialList(MultipartFile file) throws IOException, InvalidFormatException {
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
                if (null == row.getCell(3) || "".equals(row.getCell(3).toString().trim())) {
                    continue;
                }
                Material material = Material.newInstance();
                String code = row.getCell(3).toString().trim();
                if (!codeList.contains(code) && !targetCodeList.contains(code)) {
                    material.setOriginCode(code);
                    material.setDrawingNo(code);
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
    public void importStructureInfo(MultipartFile file) throws IOException, InvalidFormatException {
        Sheet sheet = formatExcelBOM(file, "BasicData");
        List<Material> materialList = new ArrayList<>(3000);
        List<Material> updateMaterialList = new ArrayList<>(3000);
        List<String> codeList = materialRepository.findAllMaterialCode();

        int rowIndex = 0;
        for (Row row : sheet) {
            if (rowIndex < 1) {
                rowIndex++;
            } else {
                if (null == row.getCell(1) || "".equals(row.getCell(1).toString().trim())) {
                    continue;
                }
                Material material;
                String code = row.getCell(1).toString().trim();
                if (!codeList.contains(code)) {
                    material = Material.newInstance();
                    material.setCode(code);
                    if (null != row.getCell(2) && !"".equals(row.getCell(2).toString().trim())) {
                        material.setName(row.getCell(2).toString().trim());
                    }
                    if (null != row.getCell(4) && !"".equals(row.getCell(4).toString().trim())) {
                        material.setSpecification(row.getCell(4).toString().trim());
                    }
                    if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                        material.setModel(row.getCell(5).toString().trim());
                    }
                    if (null != row.getCell(6) && !"".equals(row.getCell(6).toString().trim())) {
                        material.setInternational(row.getCell(6).toString().trim());
                    }
                    if (null != row.getCell(7) && !"".equals(row.getCell(7).toString().trim())) {
                        material.setDrawingNo(row.getCell(7).toString().trim());
                    }
                    if (null != row.getCell(8) && !"".equals(row.getCell(8).toString().trim())) {
                        material.setReference(row.getCell(8).toString().trim());
                    }
                    if (null != row.getCell(9) && !"".equals(row.getCell(9).toString().trim())) {
                        material.setGeneralSort(row.getCell(9).toString().trim());
                    }
                    if (null != row.getCell(10) && !"".equals(row.getCell(10).toString().trim())) {
                        material.setInspectMark(row.getCell(10).toString().trim());
                    }
                    if (null != row.getCell(11) && !"".equals(row.getCell(11).toString().trim())) {
                        material.setBatchMark(row.getCell(11).toString().trim());
                    }
                    if (null != row.getCell(12) && !"".equals(row.getCell(12).toString().trim())) {
                        material.setInventoryUnit(row.getCell(12).toString().trim());
                    }
                    if (null != row.getCell(13) && !"".equals(row.getCell(13).toString().trim())) {
                        material.setDefRepository(row.getCell(13).toString().trim());
                    }
                    if (null != row.getCell(14) && !"".equals(row.getCell(14).toString().trim())) {
                        material.setVirtualPartMark(row.getCell(14).toString().trim());
                    }
                    if (null != row.getCell(15) && !"".equals(row.getCell(15).toString().trim())) {
                        material.setSourceMark(row.getCell(15).toString().trim());
                    }
                    if (null != row.getCell(16) && !"".equals(row.getCell(16).toString().trim())) {
                        material.setOutSource(row.getCell(16).toString().trim());
                    }
                    if (null != row.getCell(17) && !"".equals(row.getCell(17).toString().trim())) {
                        material.setRespDept(row.getCell(17).toString().trim());
                    }
                    if (null != row.getCell(18) && !"".equals(row.getCell(18).toString().trim())) {
                        material.setRespCompany(row.getCell(18).toString().trim());
                    }
                    if (null != row.getCell(19) && !"".equals(row.getCell(19).toString().trim())) {
                        material.setKeyPartSort(row.getCell(19).toString().trim());
                    }
                    if (null != row.getCell(20) && !"".equals(row.getCell(20).toString().trim())) {
                        material.setKeyPartMark(row.getCell(20).toString().trim());
                    }
                    if (null != row.getCell(21) && !"".equals(row.getCell(21).toString().trim())) {
                        material.setPurchaseSort(row.getCell(21).toString().trim());
                    }
                    if (null != row.getCell(22) && !"".equals(row.getCell(22).toString().trim())) {
                        material.setFixedAdvTime(row.getCell(22).toString().trim());
                    }
                    if (null != row.getCell(23) && !"".equals(row.getCell(23).toString().trim())) {
                        material.setDescription(row.getCell(23).toString().trim());
                    }
                    if (null != row.getCell(24) && !"".equals(row.getCell(24).toString().trim())) {
                        material.setQualifiedMark(row.getCell(24).toString().trim());
                    }
                    if (null != row.getCell(25) && !"".equals(row.getCell(25).toString().trim())) {
                        material.setPurchaseMark(row.getCell(25).toString().trim());
                    } else {
                        material.setPurchaseMark("Y");
                    }
                    if (null != row.getCell(26) && !"".equals(row.getCell(26).toString().trim())) {
                        material.setGroupPurMark(row.getCell(26).toString().trim());
                    }
                    if (null != row.getCell(27) && !"".equals(row.getCell(27).toString().trim())) {
                        material.setOwnPurMark(row.getCell(27).toString().trim());
                    }
                    if (null != row.getCell(28) && !"".equals(row.getCell(28).toString().trim())) {
                        material.setPlanner(row.getCell(28).toString().trim());
                    }
                    if (null != row.getCell(29) && !"".equals(row.getCell(29).toString().trim())) {
                        material.setResourceMark(row.getCell(29).toString().trim());
                    }
                    material.setTechnologyStatus(1);
                    material.setQualityStatus(1);
//                    if ("P".equals(material.getSourceMark())) {
//                        material.setProduceStatus(1);
//                    } else if ("M".equals(material.getSourceMark())) {
//                        material.setPurchaseStatus(1);
//                    }
                    material.setPurchaseStatus(1);
                    material.setAssemblyStatus(1);
                    material.setProduceStatus(1);
                    material.setExportStatus(2);
                    materialList.add(material);
                } else {
                    material = materialRepository.findMaterialByCode(code);
                    if (null != row.getCell(2) && !"".equals(row.getCell(2).toString().trim())) {
                        material.setName(row.getCell(2).toString().trim());
                    }
                    if (null != row.getCell(4) && !"".equals(row.getCell(4).toString().trim())) {
                        material.setSpecification(row.getCell(4).toString().trim());
                    }
                    if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                        material.setModel(row.getCell(5).toString().trim());
                    }
                    if (null != row.getCell(6) && !"".equals(row.getCell(6).toString().trim())) {
                        material.setInternational(row.getCell(6).toString().trim());
                    }
                    material.setDrawingNo(code);
                    if (null != row.getCell(8) && !"".equals(row.getCell(8).toString().trim())) {
                        material.setReference(row.getCell(8).toString().trim());
                    }
                    if (null != row.getCell(9) && !"".equals(row.getCell(9).toString().trim())) {
                        material.setGeneralSort(row.getCell(9).toString().trim());
                    }
                    if (null != row.getCell(10) && !"".equals(row.getCell(10).toString().trim())) {
                        material.setInspectMark(row.getCell(10).toString().trim());
                    }
                    if (null != row.getCell(11) && !"".equals(row.getCell(11).toString().trim())) {
                        material.setBatchMark(row.getCell(11).toString().trim());
                    }
                    if (null != row.getCell(12) && !"".equals(row.getCell(12).toString().trim())) {
                        material.setInventoryUnit(row.getCell(12).toString().trim());
                    }
                    if (null != row.getCell(13) && !"".equals(row.getCell(13).toString().trim())) {
                        material.setDefRepository(row.getCell(13).toString().trim());
                    }
                    if (null != row.getCell(14) && !"".equals(row.getCell(14).toString().trim())) {
                        material.setVirtualPartMark(row.getCell(14).toString().trim());
                    }
                    if (null != row.getCell(15) && !"".equals(row.getCell(15).toString().trim())) {
                        material.setSourceMark(row.getCell(15).toString().trim());
                    }
                    if (null != row.getCell(16) && !"".equals(row.getCell(16).toString().trim())) {
                        material.setOutSource(row.getCell(16).toString().trim());
                    }
                    if (null != row.getCell(17) && !"".equals(row.getCell(17).toString().trim())) {
                        material.setRespDept(row.getCell(17).toString().trim());
                    }
                    if (null != row.getCell(18) && !"".equals(row.getCell(18).toString().trim())) {
                        material.setRespCompany(row.getCell(18).toString().trim());
                    }
                    if (null != row.getCell(19) && !"".equals(row.getCell(19).toString().trim())) {
                        material.setKeyPartSort(row.getCell(19).toString().trim());
                    }
                    if (null != row.getCell(20) && !"".equals(row.getCell(20).toString().trim())) {
                        material.setKeyPartMark(row.getCell(20).toString().trim());
                    }
                    if (null != row.getCell(21) && !"".equals(row.getCell(21).toString().trim())) {
                        material.setPurchaseSort(row.getCell(21).toString().trim());
                    }
                    if (null != row.getCell(22) && !"".equals(row.getCell(22).toString().trim())) {
                        material.setFixedAdvTime(row.getCell(22).toString().trim());
                    }
                    if (null != row.getCell(23) && !"".equals(row.getCell(23).toString().trim())) {
                        material.setDescription(row.getCell(23).toString().trim());
                    }
                    if (null != row.getCell(24) && !"".equals(row.getCell(24).toString().trim())) {
                        material.setQualifiedMark(row.getCell(24).toString().trim());
                    }
                    if (null != row.getCell(25) && !"".equals(row.getCell(25).toString().trim())) {
                        material.setPurchaseMark(row.getCell(25).toString().trim());
                    } else {
                        material.setPurchaseMark("Y");
                    }
                    if (null != row.getCell(26) && !"".equals(row.getCell(26).toString().trim())) {
                        material.setGroupPurMark(row.getCell(26).toString().trim());
                    }
                    if (null != row.getCell(27) && !"".equals(row.getCell(27).toString().trim())) {
                        material.setOwnPurMark(row.getCell(27).toString().trim());
                    }
                    if (null != row.getCell(28) && !"".equals(row.getCell(28).toString().trim())) {
                        material.setPlanner(row.getCell(28).toString().trim());
                    }
                    if (null != row.getCell(29) && !"".equals(row.getCell(29).toString().trim())) {
                        material.setResourceMark(row.getCell(29).toString().trim());
                    }
                    updateMaterialList.add(material);
                }
            }
        }
        materialRepository.saveAll(materialList);
//        materialRepository.saveAll(updateMaterialList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Workbook exportMaterialsByDepartment(Integer deptMark, Integer perfectStatus) {
        Workbook workbook = null;
        if (3 == deptMark) {
            workbook = buildPurchaseWorkbookToPrefect(perfectStatus);
        } else if (5 == deptMark) {
            workbook = buildProduceWorkbookToPrefect();
        } else if (4 == deptMark) {
            workbook = buildAssemblyWorkbookToPrefect();
        }
        return workbook;
    }

    private Workbook buildPurchaseWorkbookToPrefect(int perfectStatus) {
        List<Material> materialList;
        if (2 == perfectStatus) {
            materialList = materialRepository.findAllByPurchase();
        } else {
            materialList = materialRepository.findAllByPurchaseAndPerfectStatus(perfectStatus);
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");

        int rowIndex = 0;

        XSSFRow row0 = sheet.createRow(rowIndex);
        XSSFCell cell00 = row0.createCell(0);
        cell00.setCellValue("部套号");
        XSSFCell cell01 = row0.createCell(1);
        cell01.setCellValue("物料号");
        XSSFCell cell02 = row0.createCell(2);
        cell02.setCellValue("图号");
        XSSFCell cell03 = row0.createCell(3);
        cell03.setCellValue("物料名称");
        XSSFCell cell04 = row0.createCell(4);
        cell04.setCellValue("货源");
        XSSFCell cell05 = row0.createCell(5);
        cell05.setCellValue("采购分类");
        XSSFCell cell06 = row0.createCell(6);
        cell06.setCellValue("可采购标记");
        XSSFCell cell09 = row0.createCell(7);
        cell09.setCellValue("固定提前期");

        for (Material material : materialList) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(material.getStructureNo());
            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(material.getCode());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(material.getDrawingNo());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(material.getName());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(material.getResourceMark());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(material.getPurchaseSort());
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(material.getPurchaseMark());
            XSSFCell cell9 = row.createCell(7);
            cell9.setCellValue(material.getFixedAdvTime());
        }

        return workbook;
    }

    private Workbook buildProduceWorkbookToPrefect() {
        List<Material> materialList = materialRepository.findAllByProduceAndPerfectStatus(0);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");

        int rowIndex = 0;

        XSSFRow row0 = sheet.createRow(rowIndex);
        XSSFCell cell00 = row0.createCell(0);
        cell00.setCellValue("部套号");
        XSSFCell cell01 = row0.createCell(1);
        cell01.setCellValue("物料号");
        XSSFCell cell02 = row0.createCell(2);
        cell02.setCellValue("图号");
        XSSFCell cell03 = row0.createCell(3);
        cell03.setCellValue("物料名称");
        XSSFCell cell04 = row0.createCell(4);
        cell04.setCellValue("货源");
        XSSFCell cell05 = row0.createCell(5);
        cell05.setCellValue("外协标记");
        XSSFCell cell06 = row0.createCell(6);
        cell06.setCellValue("计划员");
        XSSFCell cell09 = row0.createCell(7);
        cell09.setCellValue("固定提前期");

        for (Material material : materialList) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(material.getStructureNo());
            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(material.getCode());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(material.getDrawingNo());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(material.getName());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(material.getResourceMark());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(material.getOutSource());
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(material.getPlanner());
            XSSFCell cell9 = row.createCell(7);
            cell9.setCellValue(material.getFixedAdvTime());
        }

        return workbook;
    }

    private Workbook buildAssemblyWorkbookToPrefect() {
        List<Material> materialList = materialRepository.findAllByAssemblyStatus(0);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data");

        int rowIndex = 0;

        XSSFRow row0 = sheet.createRow(rowIndex);
        XSSFCell cell00 = row0.createCell(0);
        cell00.setCellValue("部套号");
        XSSFCell cell01 = row0.createCell(1);
        cell01.setCellValue("物料号");
        XSSFCell cell02 = row0.createCell(2);
        cell02.setCellValue("图号");
        XSSFCell cell03 = row0.createCell(3);
        cell03.setCellValue("物料名称");
        XSSFCell cell04 = row0.createCell(4);
        cell04.setCellValue("货源");
        XSSFCell cell05 = row0.createCell(5);
        cell05.setCellValue("默认仓库");

        for (Material material : materialList) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            XSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(material.getStructureNo());
            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(material.getCode());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(material.getDrawingNo());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(material.getName());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(material.getResourceMark());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(material.getDefRepository());
        }

        return workbook;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importPerfectedMaterialsByDepartment(MultipartFile file, String sheetName, Integer deptMark) throws IOException, InvalidFormatException {
        Sheet sheet = formatExcelBOM(file, "data");
        int rowIndex = 0;
        if (3 == deptMark) {
            for (Row row : sheet) {
                if (0 == rowIndex) {
                    rowIndex++;
                } else {

                    if (null != row.getCell(1) && !"".equals(row.getCell(1).toString().trim())) {

                        String code = row.getCell(1).toString().trim();

                        int perfectStatus = Constant.Material.PerfectStatus.PERFECTED;

                        Material material = materialRepository.findMaterialByCode(code);
                        material.setPurchaseMark("Y");
                        if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                            material.setPurchaseSort(row.getCell(5).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        if (null != row.getCell(7) && !"".equals(row.getCell(7).toString().trim())) {
                            material.setFixedAdvTime(row.getCell(7).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        material.setUpdateAt(LocalDateTime.now());
                        material.setPurchaseStatus(perfectStatus);
                        checkExportStatus(material, 3);
                        materialRepository.save(material);
                    }
                }
            }
        } else if (5 == deptMark) {
            for (Row row : sheet) {
                if (0 == rowIndex) {
                    rowIndex++;
                } else {
                    if (null != row.getCell(1) && !"".equals(row.getCell(1).toString().trim())) {

                        String code = row.getCell(1).toString().trim();

                        int perfectStatus = Constant.Material.PerfectStatus.PERFECTED;

                        Material material = materialRepository.findMaterialByCode(code);
                        if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                            material.setOutSource(row.getCell(5).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        if (null != row.getCell(6) && !"".equals(row.getCell(6).toString().trim())) {
                            material.setPlanner(row.getCell(6).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        if (null != row.getCell(7) && !"".equals(row.getCell(7).toString().trim())) {
                            material.setFixedAdvTime(row.getCell(7).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        material.setUpdateAt(LocalDateTime.now());
                        material.setProduceStatus(perfectStatus);
                        checkExportStatus(material, 5);
                        materialRepository.save(material);
                    }
                }
            }
        } else if (4 == deptMark) {
            for (Row row : sheet) {
                if (0 == rowIndex) {
                    rowIndex++;
                } else {
                    if (null != row.getCell(1) && !"".equals(row.getCell(1).toString().trim())) {

                        String code = row.getCell(1).toString().trim();

                        int perfectStatus = Constant.Material.PerfectStatus.PERFECTED;

                        Material material = materialRepository.findMaterialByCode(code);
                        if (null != row.getCell(5) && !"".equals(row.getCell(5).toString().trim())) {
                            material.setDefRepository(row.getCell(5).toString().trim());
                        } else {
                            perfectStatus = Constant.Material.PerfectStatus.IMPERFECT;
                        }
                        material.setUpdateAt(LocalDateTime.now());
                        material.setAssemblyStatus(perfectStatus);
                        checkExportStatus(material, 4);
                        materialRepository.save(material);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbook exportMaterialList() {
        List<Material> materialList = materialRepository.findAllByExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
        Workbook workbook = createWorkbook(materialList);
        Set<String> tokenSet = new HashSet<>();
        for (Material material : materialList) {
            tokenSet.add(material.getToken());
            material.setExportStatus(Constant.Material.ExportStatus.EXPORTED);
        }
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        for (String token : tokenSet) {
            Statistic statistic = statisticRepository.findOneByToken(token);
            statistic.setExportAt(dateTime);
            statisticRepository.save(statistic);
        }
        materialRepository.saveAll(materialList);
        return workbook;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbook exportAll() {
        List<Material> materialList = materialRepository.findAllAndOrderById();
        return createWorkbook(materialList);
    }

    private void checkExportStatus(Material material, Integer dept) {
        if (Constant.Material.PerfectStatus.PERFECTED == material.getTechnologyStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getQualityStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getPurchaseStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getAssemblyStatus()
                && Constant.Material.PerfectStatus.PERFECTED == material.getProduceStatus()) {
            material.setExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
        }
        Statistic statistic = statisticRepository.findOneByToken(material.getToken());
        if (null != statistic) {
            LocalDateTime dateTime = LocalDateTime.now().withNano(0);
            switch (dept) {
                case 1:
                    statistic.setTechnologyCompleteAt(dateTime);
                    break;
                case 3:
                    statistic.setPurchaseCompleteAt(dateTime);
                    break;
                case 4:
                    statistic.setAssemblyCompleteAt(dateTime);
                    break;
                case 5:
                    statistic.setProduceCompleteAt(dateTime);
                    break;
                default:
                    break;
            }
            statisticRepository.save(statistic);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterial(Material material) {
        materialRepository.save(material);
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

        if ("6205".equals(targetMater.getRespDept())) {
            targetMater.setDefRepository("801");
        } else if ("6206".equals(targetMater.getRespDept())) {
            targetMater.setDefRepository("802");
        } else if ("6207".equals(targetMater.getRespDept())) {
            targetMater.setDefRepository("803");
        } else if ("6209".equals(targetMater.getRespDept())) {
            targetMater.setDefRepository("80");
        }

        if ((targetMater.getCode().startsWith("CB")
                || targetMater.getCode().startsWith("CS")
                || targetMater.getCode().startsWith("CM")) && targetMater.getCode().endsWith(".000.000")) {
            targetMater.setDefRepository("01");
        }

        targetMater.setKeyPartMark(material.getKeyPartMark());
        targetMater.setKeyPartSort(material.getKeyPartSort());
        targetMater.setVirtualPartMark(material.getVirtualPartMark());
        targetMater.setQualifiedMark(material.getQualifiedMark());
        targetMater.setTechnologyStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater, 1);
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
        checkExportStatus(targetMater, 2);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialPurchase(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setPurchaseSort(material.getPurchaseSort());
        targetMater.setPurchaseMark(material.getPurchaseMark());
        targetMater.setFixedAdvTime(material.getFixedAdvTime());
        targetMater.setPurchaseStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater, 3);
        materialRepository.save(targetMater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialAssembly(Material material) {
        Material targetMater = materialRepository.findMaterialByCode(material.getCode());
        targetMater.setDefRepository(material.getDefRepository());
        targetMater.setAssemblyStatus(Constant.Material.PerfectStatus.PERFECTED);
        targetMater.setUpdateAt(LocalDateTime.now());
        checkExportStatus(targetMater, 4);
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
        checkExportStatus(targetMater, 5);
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

    @Override
    public PageContext<Material> findGuaranteeMaterialListByPagination(int pageIndex, int pageSize, User user) {
        // 指定排序参数对象：根据id，进行升序查询
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        /*
         * 封装分页实体 Pageable
         * 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页)
         * 参数二：表示每页展示多少数据，现在设置每页展示100条数据
         * 参数三：封装排序对象，根据该对象的参数指定根据id升序查询
         * */
        Page<Material> materialPage = null;

        if (user.getRoles().getOrDefault(Constant.UserRoles.TECHNOLOGY_EMPLOYEE, false)
                || user.getRoles().getOrDefault(Constant.UserRoles.GUARANTEE_EMPLOYEE, false)) {
            materialPage = materialRepository.findAll((Specification<Material>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.EXPORT_STATUS), 0));
                predicates.add(criteriaBuilder.equal(root.get(Constant.Material.STRUCTURE_NO), "SG-9999"));
                return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
            }, PageRequest.of(pageIndex - 1, pageSize, sort));
        }
        PageContext<Material> pageContext = new PageContext<>();
        pageContext.setIndex(pageIndex);
        if (null != materialPage) {
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
        XSSFCell cell08 = row0.createCell(6);
        cell08.setCellValue("国标号");
        XSSFCell cell09 = row0.createCell(7);
        cell09.setCellValue("图号");
        XSSFCell cell010 = row0.createCell(8);
        cell010.setCellValue("参考号");
        XSSFCell cell011 = row0.createCell(9);
        cell011.setCellValue("普通分类");
        XSSFCell cell020 = row0.createCell(10);
        cell020.setCellValue("质检标记");
        XSSFCell cell021 = row0.createCell(11);
        cell021.setCellValue("批次标记");
        XSSFCell cell012 = row0.createCell(12);
        cell012.setCellValue("库存单位");
        XSSFCell cell026 = row0.createCell(13);
        cell026.setCellValue("默认仓库编码");
        XSSFCell cell018 = row0.createCell(14);
        cell018.setCellValue("虚拟件标记");
        XSSFCell cell013 = row0.createCell(15);
        cell013.setCellValue("采购自制件标记");
        XSSFCell cell027 = row0.createCell(16);
        cell027.setCellValue("外协标记");
        XSSFCell cell014 = row0.createCell(17);
        cell014.setCellValue("责任部门");
        XSSFCell cell015 = row0.createCell(18);
        cell015.setCellValue("责任公司");
        XSSFCell cell017 = row0.createCell(19);
        cell017.setCellValue("关键件大类");
        XSSFCell cell016 = row0.createCell(20);
        cell016.setCellValue("关键件标记");
        XSSFCell cell022 = row0.createCell(21);
        cell022.setCellValue("采购分类");
        XSSFCell cell029 = row0.createCell(22);
        cell029.setCellValue("固定提前期");
        XSSFCell cell07 = row0.createCell(23);
        cell07.setCellValue("具体描述");
        XSSFCell cell019 = row0.createCell(24);
        cell019.setCellValue("合批标记");
        XSSFCell cell023 = row0.createCell(25);
        cell023.setCellValue("可采购标记");
        XSSFCell cell024 = row0.createCell(26);
        cell024.setCellValue("可集采标记");
        XSSFCell cell025 = row0.createCell(27);
        cell025.setCellValue("可自采标记");
        XSSFCell cell028 = row0.createCell(28);
        cell028.setCellValue("计划员");
        XSSFCell cell037 = row0.createCell(29);
        cell037.setCellValue("下发部门");
        XSSFCell cell033 = row0.createCell(30);
        cell033.setCellValue("货源");
        XSSFCell cell034 = row0.createCell(31);
        cell034.setCellValue("部套号");
        XSSFCell cell035 = row0.createCell(32);
        cell035.setCellValue("创建时间");
        XSSFCell cell036 = row0.createCell(33);
        cell036.setCellValue("完善时间");

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
            XSSFCell cell8 = row.createCell(6);
            cell8.setCellValue(material.getInternational());
            XSSFCell cell9 = row.createCell(7);
            cell9.setCellValue(material.getCode());
            XSSFCell cell10 = row.createCell(8);
            if (null == material.getOriginCode()) {
                cell10.setCellValue(material.getCode());
            } else {
                cell10.setCellValue(material.getOriginCode());
            }
            XSSFCell cell11 = row.createCell(9);
            cell11.setCellValue(material.getGeneralSort());
            XSSFCell cell20 = row.createCell(10);
            cell20.setCellValue(material.getInspectMark());
            XSSFCell cell21 = row.createCell(11);
            cell21.setCellValue(material.getBatchMark());
            XSSFCell cell12 = row.createCell(12);
            cell12.setCellValue(material.getInventoryUnit());
            XSSFCell cell26 = row.createCell(13);
            cell26.setCellValue(material.getDefRepository());
            XSSFCell cell18 = row.createCell(14);
            cell18.setCellValue(material.getVirtualPartMark());
            XSSFCell cell13 = row.createCell(15);
            cell13.setCellValue(material.getSourceMark());
            XSSFCell cell27 = row.createCell(16);
            cell27.setCellValue(material.getOutSource());
            XSSFCell cell14 = row.createCell(17);
            cell14.setCellValue(material.getRespDept());
            XSSFCell cell15 = row.createCell(18);
            cell15.setCellValue(material.getRespCompany());
            XSSFCell cell17 = row.createCell(19);
            cell17.setCellValue(material.getKeyPartSort());
            XSSFCell cell16 = row.createCell(20);
            cell16.setCellValue(material.getKeyPartMark());
            XSSFCell cell22 = row.createCell(21);
            if ("P".equals(material.getSourceMark())) {
                cell22.setCellValue(material.getPurchaseSort());
            }
            XSSFCell cell29 = row.createCell(22);
            cell29.setCellValue(material.getFixedAdvTime());
            XSSFCell cell7 = row.createCell(23);
            cell7.setCellValue(material.getDescription());
            XSSFCell cell19 = row.createCell(24);
            cell19.setCellValue(material.getQualifiedMark());
            XSSFCell cell23 = row.createCell(25);
            cell23.setCellValue("Y");
            XSSFCell cell24 = row.createCell(26);
            cell24.setCellValue(material.getGroupPurMark());
            XSSFCell cell25 = row.createCell(27);
            if ("M".equals(material.getSourceMark())) {
                cell25.setCellValue("Y");
            } else {
                cell25.setCellValue(material.getOwnPurMark());
            }
            XSSFCell cell28 = row.createCell(28);
            // 因为自制件计划员有默认值，当该物料为非采购件时，不能输入默认值，以下列必须为空
            if ("M".equals(material.getSourceMark())) {
                cell28.setCellValue(material.getPlanner());
            }
            XSSFCell cell37 = row.createCell(29);
            cell37.setCellValue(material.getDeliveryDept());
            XSSFCell cell33 = row.createCell(30);
            cell33.setCellValue(material.getResourceMark());
            XSSFCell cell34 = row.createCell(31);
            cell34.setCellValue(material.getStructureNo());
            XSSFCell cell35 = row.createCell(32);
            cell35.setCellValue(LocalDate.from(material.getCreateAt()).toString());
            XSSFCell cell36 = row.createCell(33);
            cell36.setCellValue(LocalDate.from(material.getUpdateAt()).toString());
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

    @Override
    public Perfect statisticCount() {
        Perfect perfect = new Perfect();
        int count = materialRepository.findCountOfMaterial();
        perfect.setCount(count);

        List<DeptPerfect> deptPerfects = new ArrayList<>();

        int technologyImperfect = materialRepository.findCountOfTechnology(0);
        int technologyPerfected = materialRepository.findCountOfTechnology(1);
        deptPerfects.add(new DeptPerfect("技术中心", technologyImperfect, technologyPerfected));

        int qualityImperfect = materialRepository.findCountOfQuality(0);
        int qualityPerfected = materialRepository.findCountOfQuality(1);
        deptPerfects.add(new DeptPerfect("质量合规部", qualityImperfect, qualityPerfected));

        int purchaseImperfect = materialRepository.findCountOfPurchase(0);
        int purchasePerfected = materialRepository.findCountOfPurchase(1);
        deptPerfects.add(new DeptPerfect("采购部", purchaseImperfect, purchasePerfected));

        int assemblyImperfect = materialRepository.findCountOfAssembly(0);
        int assemblyPerfected = materialRepository.findCountOfAssembly(1);
        deptPerfects.add(new DeptPerfect("集配中心", assemblyImperfect, assemblyPerfected));

        int produceImperfect = materialRepository.findCountOfProduce(0);
        int producePerfected = materialRepository.findCountOfProduce(1);
        deptPerfects.add(new DeptPerfect("生产部", produceImperfect, producePerfected));

        perfect.setDeptPerfects(deptPerfects);

        return perfect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmGuaranteeMaterialList(List<Material> materialList) {
        for (Material material : materialList) {
            Material targetMaterial = materialRepository.findMaterialByCode(material.getCode());
            targetMaterial.setExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
            materialRepository.save(targetMaterial);
        }
    }
}
