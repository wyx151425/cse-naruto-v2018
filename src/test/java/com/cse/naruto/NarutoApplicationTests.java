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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

//    @Test
//    public void demos() {
//        List<Material> materialList = materialRepository.findAll();
//        for (Material material : materialList) {
//            material.setStatus(2);
//        }
//        materialRepository.saveAll(materialList);
//    }

//    private Permission createPermission() {
//        Permission permission = new Permission();
//        permission.setObjectId(Generator.getObjectId());
//        permission.setStatus(Constant.PermissionStatus.GENERAL);
//        LocalDateTime dateTime = LocalDateTime.now();
//        permission.setCreateAt(dateTime);
//        permission.setUpdateAt(dateTime);
//        return permission;
//    }
//
//    private User createUser() {
//        User user = new User();
//        user.setObjectId(Generator.getObjectId());
//        user.setStatus(Constant.UserStatus.GENERAL);
//        LocalDateTime dateTime = LocalDateTime.now();
//        user.setCreateAt(dateTime);
//        user.setUpdateAt(dateTime);
//        return user;
//    } 6110

    @Test
    public void contextLoads() {
    }

//    @Test
//    public void addPermission() {
//        List<Permission> permissionList = new ArrayList<>();
//
//        Permission permission1 = createPermission();
//        permission1.setRole("ROLE_USER");
//        permission1.setName("MATERIAL_STANDARD");
//        permissionList.add(permission1);
//
//        Permission permission2 = createPermission();
//        permission2.setRole("ROLE_TECHNOLOGY_EMPLOYEE");
//        permission2.setName("MATERIAL_EDIT_TECH_PART");
//        permissionList.add(permission2);
//
//        Permission permission3 = createPermission();
//        permission3.setRole("ROLE_QUALITY_EMPLOYEE");
//        permission3.setName("MATERIAL_EDIT_QA_ENV_PART");
//        permissionList.add(permission3);
//
//        Permission permission4 = createPermission();
//        permission4.setRole("ROLE_PURCHASE_EMPLOYEE");
//        permission4.setName("MATERIAL_EDIT_PURCHASE_PART");
//        permissionList.add(permission4);
//
//        Permission permission5 = createPermission();
//        permission5.setRole("ROLE_ASSEMBLY_EMPLOYEE");
//        permission5.setName("MATERIAL_EDIT_ASSEMBLY_PART");
//        permissionList.add(permission5);
//
//        Permission permission6 = createPermission();
//        permission6.setRole("ROLE_PRODUCE_EMPLOYEE");
//        permission6.setName("MATERIAL_PRO_OPE_PART");
//        permissionList.add(permission6);
//
//        permissionRepository.saveAll(permissionList);
//    }

//    @Test
//    public void addUser() {
//        User user1 = new User();
//        user1.setObjectId(Generator.getObjectId());
//        user1.setStatus(1);
//        LocalDateTime dateTime1 = LocalDateTime.now();
//        user1.setCreateAt(dateTime1);
//        user1.setUpdateAt(dateTime1);
//        user1.setCode("A0000115");
//        user1.setPassword("123456");
//        user1.setName("吕律");
//        user1.setRole("ROLE_USER,ROLE_TECHNOLOGY_EMPLOYEE");
//        userRepository.save(user1);
//        User user2 = new User();
//        user2.setObjectId(Generator.getObjectId());
//        user2.setStatus(1);
//        LocalDateTime dateTime2 = LocalDateTime.now();
//        user2.setCreateAt(dateTime2);
//        user2.setUpdateAt(dateTime2);
//        user2.setCode("QD0081");
//        user2.setPassword("123456");
//        user2.setName("王灿");
//        user2.setRole("ROLE_USER,ROLE_PURCHASE_EMPLOYEE");
//        userRepository.save(user2);
//        User user3 = new User();
//        user3.setObjectId(Generator.getObjectId());
//        user3.setStatus(1);
//        LocalDateTime dateTime3 = LocalDateTime.now();
//        user3.setCreateAt(dateTime3);
//        user3.setUpdateAt(dateTime3);
//        user3.setCode("QD0837");
//        user3.setPassword("123456");
//        user3.setName("张俊华");
//        user3.setRole("ROLE_USER,ROLE_ASSEMBLY_EMPLOYEE");
//        userRepository.save(user3);
//        User user5 = new User();
//        user5.setObjectId(Generator.getObjectId());
//        user5.setStatus(1);
//        LocalDateTime dateTime5 = LocalDateTime.now();
//        user5.setCreateAt(dateTime5);
//        user5.setUpdateAt(dateTime5);
//        user5.setCode("QD0753");
//        user5.setPassword("123456");
//        user5.setName("刘义芳");
//        user5.setRole("ROLE_USER,ROLE_ASSEMBLY_EMPLOYEE");
//        userRepository.save(user5);
//        User user4 = new User();
//        user4.setObjectId(Generator.getObjectId());
//        user4.setStatus(1);
//        LocalDateTime dateTime4 = LocalDateTime.now();
//        user4.setCreateAt(dateTime4);
//        user4.setUpdateAt(dateTime4);
//        user4.setCode("QD0624");
//        user4.setPassword("123456");
//        user4.setName("宫美芳");
//        user4.setRole("ROLE_USER,ROLE_PRODUCE_EMPLOYEE");
//        userRepository.save(user4);
//    }

//    @Test
//    public void addAdmin() {
//        User admin = createUser();
//        admin.setCode("admin");
//        admin.setPassword("151425");
//        admin.setName("admin");
//        admin.setRole("ROLE_USER,ROLE_TECHNOLOGY_EMPLOYEE,ROLE_QUALITY_EMPLOYEE,ROLE_PURCHASE_EMPLOYEE,ROLE_ASSEMBLY_EMPLOYEE,ROLE_PRODUCE_EMPLOYEE");
//        userRepository.save(admin);
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
//    public void cnm() {
//        List<Material> materials = materialRepository.findAll();
//        for (Material material : materials) {
////            material.setTechnologyStatus(0);
////            material.setQualityStatus(0);
////            material.setPurchaseStatus(0);
////            material.setAssemblyStatus(0);
////            material.setProduceStatus(0);
//            material.setExportStatus(0);
//        }
//        materialRepository.saveAll(materials);
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
//    public void updateCompany() {
//        List<Material> materials = materialRepository.findAll();
//        for (Material material : materials) {
//            material.setRespCompany("03");
//        }
//        materialRepository.saveAll(materials);
//    }

//    @Test
//    public void updateCode() {
//        User user = userRepository.findById(22).get();
//        user.setCode(user.getCode().replace(".0", ""));
//        userRepository.save(user);
//    }

    @Test
    public void updateMater() {
        Material material = materialRepository.findMaterialByCode("0979765-7.1");
        material.setName("填料函");
        materialRepository.save(material);
    }
}
