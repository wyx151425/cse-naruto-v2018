package com.cse.naruto;

import com.cse.naruto.model.Material;
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
//        permission.setStatus(Constant.PermissionStatus.COMMON);
//        LocalDateTime dateTime = LocalDateTime.now();
//        permission.setCreateAt(dateTime);
//        permission.setUpdateAt(dateTime);
//        return permission;
//    }
//
//    private User createUser() {
//        User user = new User();
//        user.setObjectId(Generator.getObjectId());
//        user.setStatus(Constant.UserStatus.COMMON);
//        LocalDateTime dateTime = LocalDateTime.now();
//        user.setCreateAt(dateTime);
//        user.setUpdateAt(dateTime);
//        return user;
//    }

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
//        permission2.setRole("ROLE_TECH_EMPLOYEE");
//        permission2.setName("MATERIAL_EDIT_TECH_PART");
//        permissionList.add(permission2);
//
//        Permission permission3 = createPermission();
//        permission3.setRole("ROLE_QA_ENV_EMPLOYEE");
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
//        permission6.setRole("ROLE_PRO_OPE_EMPLOYEE");
//        permission6.setName("MATERIAL_PRO_OPE_PART");
//        permissionList.add(permission6);
//
//        permissionRepository.saveAll(permissionList);
//    }

//    @Test
//    public void addUser() {
//        User user1 = createUser();
//        user1.setCode("CSE1001");
//        user1.setPassword("123456");
//        user1.setName("技术中心");
//        user1.setRole("ROLE_USER,ROLE_TECH_EMPLOYEE");
//        userRepository.save(user1);
//        User user2 = createUser();
//        user2.setCode("CSE1002");
//        user2.setPassword("123456");
//        user2.setName("质量环保");
//        user2.setRole("ROLE_USER,ROLE_QA_ENV_EMPLOYEE");
//        userRepository.save(user2);
//        User user3 = createUser();
//        user3.setCode("CSE1003");
//        user3.setPassword("123456");
//        user3.setName("采购");
//        user3.setRole("ROLE_USER,ROLE_PURCHASE_EMPLOYEE");
//        userRepository.save(user3);
//        User user4 = createUser();
//        user4.setCode("CSE1004");
//        user4.setPassword("123456");
//        user4.setName("集配中心");
//        user4.setRole("ROLE_USER,ROLE_ASSEMBLY_EMPLOYEE");
//        userRepository.save(user4);
//        User user5 = createUser();
//        user5.setCode("CSE1005");
//        user5.setPassword("123456");
//        user5.setName("生产运行");
//        user5.setRole("ROLE_USER,ROLE_PRO_OPE_EMPLOYEE");
//        userRepository.save(user5);
//    }

//    @Test
//    public void addAdmin() {
//        User admin = createUser();
//        admin.setCode("admin");
//        admin.setPassword("151425");
//        admin.setName("admin");
//        admin.setRole("ROLE_USER,ROLE_TECH_EMPLOYEE,ROLE_QA_ENV_EMPLOYEE,ROLE_PURCHASE_EMPLOYEE,ROLE_ASSEMBLY_EMPLOYEE,ROLE_PRO_OPE_EMPLOYEE");
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
}
