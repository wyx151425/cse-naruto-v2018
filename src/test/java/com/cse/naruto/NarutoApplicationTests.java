package com.cse.naruto;

import com.cse.naruto.repository.MaterialRepository;
import com.cse.naruto.repository.PermissionRepository;
import com.cse.naruto.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
//        user.setCode("");
//        user.setPassword("123456");
//        user.setName("");
//        user.setRole("ROLE_USER");
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
}
