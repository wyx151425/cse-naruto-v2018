package com.cse.naruto;

import com.cse.naruto.model.Material;
import com.cse.naruto.model.User;
import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.repository.PermissionRepository;
import com.cse.naruto.repository.UserRepository;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NarutoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Test
    public void contextLoads() {
    }

//    @Test
//    public void addUser() {
//        User user = new User();
//        user.setObjectId(Generator.getObjectId());
//        user.setStatus(Constant.Status.GENERAL);
//        LocalDateTime dateTime1 = LocalDateTime.now();
//        user.setCreateAt(dateTime1);
//        user.setUpdateAt(dateTime1);
//        user.setCode("QD1034");
//        user.setPassword("123456");
//        user.setName("苏猛猛");
//        user.setRole("ROLE_USER,ROLE_TECHNOLOGY_EMPLOYEE");
//        userRepository.save(user);
//    }

//    @Test
//    public void addMaterialList() {
//        InputStream in = null;
//        try {
//            in = new FileInputStream(new File("excel.xlsx"));
//            Workbook workbook = WorkbookFactory.create(in);
//            Sheet sheet = workbook.getSheet("sheet1");
//            List<Material> materials = new ArrayList<>();
//            List<String> codeList = new ArrayList<>();
//            int index = 0;
//            for (Row row : sheet) {
//                if (index < 3) {
//                    index++;
//                } else {
//                    String code = row.getCell(1).toString().trim();
//                    if (!codeList.contains(code)) {
//                        codeList.add(code);
//                        Material material = new Material();
//                        material.setObjectId(Generator.getObjectId());
//                        material.setStatus(Constant.MaterialStatus.CAN_EXPORT);
//                        LocalDateTime dateTime = LocalDateTime.now();
//                        material.setCreateAt(dateTime);
//                        material.setUpdateAt(dateTime);
//                        material.setCompanyNo(row.getCell(0).toString().trim());
//                        material.setCode(code);
//                        material.setName(row.getCell(2).toString().trim());
//                        material.setShortName(row.getCell(3).toString().trim());
//                        material.setSpecification(row.getCell(4).toString().trim());
//                        material.setModel(row.getCell(5).toString().trim());
//                        material.setInternational(row.getCell(6).toString().trim());
//                        material.setDescription(row.getCell(23).toString().trim());
//                        material.setDrawingNo(row.getCell(7).toString().trim());
//                        material.setReference(row.getCell(8).toString().trim());
//                        material.setGeneralSort(row.getCell(9).toString().trim());
//                        material.setInventoryUnit(row.getCell(12).toString().trim());
//                        material.setSourceMark(row.getCell(15).toString().trim());
//                        material.setRespDept(row.getCell(17).toString().trim());
//                        material.setRespCompany(row.getCell(18).toString().trim());
//                        material.setKeyPartMark(row.getCell(20).toString().trim());
//                        material.setKeyPartSort(row.getCell(19).toString().trim());
//                        material.setVirtualPartMark(row.getCell(14).toString().trim());
//                        material.setQualifiedMark(row.getCell(24).toString().trim());
//                        material.setInspectMark(row.getCell(10).toString().trim());
//                        material.setBatchMark(row.getCell(11).toString().trim());
//                        material.setPurchaseSort(row.getCell(21).toString().trim());
//                        material.setPurchaseMark(row.getCell(25).toString().trim());
//                        material.setGroupPurMark(row.getCell(26).toString().trim());
//                        material.setOwnPurMark(row.getCell(27).toString().trim());
//                        material.setDefRepository(row.getCell(13).toString().trim());
//                        material.setOutSource(row.getCell(16).toString().trim());
//                        material.setPlanner(row.getCell(28).toString().trim());
//                        material.setFixedAdvTime(row.getCell(22).toString().trim());
//                        material.setTechnologyDeptMark(true);
//                        material.setQualifiedDeptMark(true);
//                        material.setPurchaseDeptMark(true);
//                        material.setAssemblyDeptMark(true);
//                        material.setOperateDeptMark(true);
//                        materials.add(material);
//                    }
//                }
//            }
//            materialRepository.saveAll(materials);
//        } catch (InvalidFormatException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//        }
//    }

//    @Test
//    public void addUserList() {
//        InputStream in = null;
//        try {
//            in = new FileInputStream(new File("users.xlsx"));
//            Workbook workbook = WorkbookFactory.create(in);
//            Sheet sheet = workbook.getSheet("sheet1");
//
//            List<User> userList = new ArrayList<>();
//            User user = new User();
//            user.setObjectId(Generator.getObjectId());
//            user.setStatus(1);
//            LocalDateTime dateTime = LocalDateTime.now();
//            user.setCreateAt(dateTime);
//            user.setUpdateAt(dateTime);
//            user.setCode("QD1010");
//            user.setName("王振琦");
//            user.setPassword("123456");
//            user.setRole("ROLE_USER,ROLE_TECHNOLOGY_EMPLOYEE");
//            userList.add(user);
//
//            int index = 0;
//            for (Row row : sheet) {
//                if (index < 1) {
//                    index++;
//                } else {
//                    if (null == row.getCell(1)) {
//                        break;
//                    }
//                    User user1 = new User();
//                    user1.setObjectId(Generator.getObjectId());
//                    user1.setStatus(1);
//                    LocalDateTime dateTime1 = LocalDateTime.now();
//                    user1.setCreateAt(dateTime1);
//                    user1.setUpdateAt(dateTime1);
//                    String code = row.getCell(1).toString().trim();
//                    if (code.startsWith("A") || code.startsWith("D")) {
//                        user1.setCode(code);
//                    } else {
//                        user1.setCode("QD" + code);
//                    }
//                    user1.setName(row.getCell(2).toString().trim());
//                    user1.setPassword("123456");
//                    user1.setRole("ROLE_USER,ROLE_TECHNOLOGY_EMPLOYEE");
//                    userList.add(user1);
//                }
//            }
//            userRepository.saveAll(userList);
//
//        } catch (InvalidFormatException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//        }
//    }

//    @Test
//    public void updateStatue() {
//        List<Material> materialList = materialRepository.findAllByAssemblyStatus(0);
//        for (Material material : materialList) {
//            material.setDefRepository("30");
//            material.setAssemblyStatus(1);
//            if (Constant.Material.PerfectStatus.PERFECTED == material.getTechnologyStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getQualityStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getPurchaseStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getAssemblyStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getProduceStatus()) {
//                material.setExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
//            }
//        }
//        materialRepository.saveAll(materialList);
//    }

//    @Test
//    public void updateStatue() {
//        List<Material> materials = materialRepository.findAllByCodeIsNull();
//        for (Material material : materials) {
//            material.setCode(material.getOriginCode());
//        }
//        materialRepository.saveAll(materials);
//    }

//    @Test
//    public void updateStatus() {
//        List<Material> materialList = materialRepository.findAllByAssemblyStatusAndIdLessThanEqual(0, 17291);
//        for (Material material : materialList) {
//            material.setDefRepository("30");
//            material.setAssemblyStatus(1);
//            if (Constant.Material.PerfectStatus.PERFECTED == material.getTechnologyStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getQualityStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getPurchaseStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getAssemblyStatus()
//                    && Constant.Material.PerfectStatus.PERFECTED == material.getProduceStatus()) {
//                material.setExportStatus(Constant.Material.ExportStatus.EXPORTABLE);
//            }
//        }
//        materialRepository.saveAll(materialList);
//    }

//    @Test
//    public void analyze() {
//        List<Material> materialList = materialRepository.findAmazing();
////        for (Material material : materialList) {
////            String oldCode = material.getOriginCode();
////            String[] codeArr = oldCode.split("-");
////            String newCode;
////            if (5 == codeArr.length) {
////                newCode = codeArr[0] + "-" + codeArr[1] + "." + codeArr[2] + "." + codeArr[3]+ "." + codeArr[4];
////            } else {
////                newCode = codeArr[0] + "-" + codeArr[1] + "." + codeArr[2] + "." + codeArr[3];
////            }
////            material.setCode(newCode);
////        }
//        System.out.println(materialList);
////        materialRepository.saveAll(materialList);
//    }

//    @Test
//    public void materialNoMatch() {
//        InputStream in = null;
//        try {
//            in = new FileInputStream(new File("MAN.xlsx"));
//            Workbook workbook = WorkbookFactory.create(in);
//            Sheet sheet = workbook.getSheet("MAN");
//            List<String> startList = new ArrayList<>();
//            int index = 0;
//            for (Row row : sheet) {
//                if (index < 1) {
//                    index++;
//                } else {
//                    if (null != row.getCell(1) && !"".equals(row.getCell(1).toString().trim())) {
//                        startList.add(row.getCell(1).toString().trim());
//                    }
//                }
//            }
//            System.out.println(startList);
//
//            in = new FileInputStream(new File("BASIC.xlsx"));
//            workbook = WorkbookFactory.create(in);
//            sheet = workbook.getSheet("BASIC");
//            List<String> materialNoList = new ArrayList<>();
//            index = 0;
//            for (Row row : sheet) {
//                if (index < 1) {
//                    index++;
//                } else {
//                    if (null != row.getCell(1) && !"".equals(row.getCell(1).toString().trim())) {
//                        String materialNo = row.getCell(1).toString().trim();
//                        for (String str : startList) {
//                            if (materialNo.startsWith(str)) {
//                                materialNoList.add(materialNo);
//                            }
//                        }
//                    }
//                }
//            }
//            System.out.println(materialNoList.size());
//
//            XSSFWorkbook outBook = new XSSFWorkbook();
//            XSSFSheet outSheet = outBook.createSheet("sheet");
//
//            int rowIndex = 0;
//
//            XSSFRow row0 = outSheet.createRow(rowIndex);
//            XSSFCell cell01 = row0.createCell(0);
//            cell01.setCellValue("物料编码");
//
//            for (String str : materialNoList) {
//                rowIndex++;
//                XSSFRow row = outSheet.createRow(rowIndex);
//                XSSFCell cell0 = row.createCell(0);
//                cell0.setCellValue(str);
//            }
//
//            OutputStream out = new FileOutputStream(new File("manb.xlsx"));
//            BufferedOutputStream buffer = new BufferedOutputStream(out);
//            buffer.flush();
//            outBook.write(buffer);
//            buffer.close();
//        } catch (InvalidFormatException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Test
//    public void prefectPBOM() throws IOException, InvalidFormatException {
//        InputStream in = new FileInputStream(new File("Excel.xlsx"));
//        Workbook workbook = WorkbookFactory.create(in);
//        Sheet sheet = workbook.getSheet("整机BOM");
//        int index = 0;
//        for (Row row : sheet) {
//            if (0 == index) {
//                row.createCell(20).setCellValue("货源");
//                row.createCell(21).setCellValue("具体描述（喷涂）");
//                row.createCell(22).setCellValue("库存单位");
//                row.createCell(23).setCellValue("采购自制标记");
//                row.createCell(24).setCellValue("责任部门");
//                row.createCell(25).setCellValue("普通分类");
//                row.createCell(26).setCellValue("关键件大类");
//                row.createCell(27).setCellValue("合批标记");
//                row.createCell(28).setCellValue("规格");
//                row.createCell(29).setCellValue("型号");
//                row.createCell(30).setCellValue("责任公司");
//            }
//            if (index < 3) {
//                index++;
//            } else {
//                String code = "";
//                if (null != row.getCell(3)) {
//                    code = row.getCell(3).toString().trim();
//                }
//                Material material = materialRepository.findMaterialByCode(code);
//                if (null != material) {
//                    if (null != material.getResourceMark()) {
//                        row.createCell(20).setCellValue(material.getResourceMark());
//                    }
//                    row.createCell(21).setCellValue(material.getDescription());
//                    row.createCell(22).setCellValue(material.getDefRepository());
//                    row.createCell(23).setCellValue(material.getSourceMark());
//                    row.createCell(24).setCellValue(material.getRespDept());
//                    row.createCell(25).setCellValue(material.getGeneralSort());
//                    row.createCell(26).setCellValue(material.getKeyPartSort());
//                    row.createCell(27).setCellValue(material.getQualifiedMark());
//                    row.createCell(28).setCellValue(material.getSpecification());
//                    row.createCell(29).setCellValue(material.getModel());
//                    row.createCell(30).setCellValue(material.getRespCompany());
//                }
//            }
//        }
//        OutputStream out = new FileOutputStream(new File("Excel.xlsx"));
//        BufferedOutputStream buffer = new BufferedOutputStream(out);
//        buffer.flush();
//        workbook.write(buffer);
//        buffer.close();
//    }
}
