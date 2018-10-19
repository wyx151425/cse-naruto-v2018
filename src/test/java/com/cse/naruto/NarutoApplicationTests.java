package com.cse.naruto;

import com.cse.naruto.model.Permission;
import com.cse.naruto.model.User;
import com.cse.naruto.repository.PermissionRepository;
import com.cse.naruto.repository.UserRepository;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    private Permission createPermission() {
        Permission permission = new Permission();
        permission.setObjectId(Generator.getObjectId());
        permission.setStatus(Constant.PermissionStatus.COMMON);
        LocalDateTime dateTime = LocalDateTime.now();
        permission.setCreateAt(dateTime);
        permission.setUpdateAt(dateTime);
        return permission;
    }

    private User createUser() {
        User user = new User();
        user.setObjectId(Generator.getObjectId());
        user.setStatus(Constant.UserStatus.COMMON);
        LocalDateTime dateTime = LocalDateTime.now();
        user.setCreateAt(dateTime);
        user.setUpdateAt(dateTime);
        return user;
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void addPermission() {
        List<Permission> permissionList = new ArrayList<>();

        Permission permission1 = createPermission();
        permission1.setRole("ROLE_USER");
        permission1.setName("MATERIAL_STANDARD");
        permissionList.add(permission1);

        Permission permission2 = createPermission();
        permission2.setRole("ROLE_TECH_EMPLOYEE");
        permission2.setName("MATERIAL_EDIT_TECH_PART");
        permissionList.add(permission2);

        Permission permission3 = createPermission();
        permission3.setRole("ROLE_QA_ENV_EMPLOYEE");
        permission3.setName("MATERIAL_EDIT_QA_ENV_PART");
        permissionList.add(permission3);

        Permission permission4 = createPermission();
        permission4.setRole("ROLE_PURCHASE_EMPLOYEE");
        permission4.setName("MATERIAL_EDIT_PURCHASE_PART");
        permissionList.add(permission4);

        Permission permission5 = createPermission();
        permission5.setRole("ROLE_ASSEMBLY_EMPLOYEE");
        permission5.setName("MATERIAL_EDIT_ASSEMBLY_PART");
        permissionList.add(permission5);

        Permission permission6 = createPermission();
        permission6.setRole("ROLE_PRO_OPE_EMPLOYEE");
        permission6.setName("MATERIAL_PRO_OPE_PART");
        permissionList.add(permission6);

        permissionRepository.saveAll(permissionList);
    }

    @Test
    public void addUser() {
        User user1 = createUser();
        user1.setCode("CSE1010");
        user1.setPassword("151425");
        user1.setName("王振琦");
        user1.setRole("ROLE_USER,ROLE_TECH_EMPLOYEE");
        userRepository.save(user1);
        User user2 = createUser();
        user2.setCode("CSE1020");
        user2.setPassword("151425");
        user2.setName("王振琦");
        user2.setRole("ROLE_USER,ROLE_QA_ENV_EMPLOYEE");
        userRepository.save(user2);
        User user3 = createUser();
        user3.setCode("CSE1030");
        user3.setPassword("151425");
        user3.setName("王振琦");
        user3.setRole("ROLE_USER,ROLE_PURCHASE_EMPLOYEE");
        userRepository.save(user3);
        User user4 = createUser();
        user4.setCode("CSE1040");
        user4.setPassword("151425");
        user4.setName("王振琦");
        user4.setRole("ROLE_USER,ROLE_ASSEMBLY_EMPLOYEE");
        userRepository.save(user4);
        User user5 = createUser();
        user5.setCode("CSE1050");
        user5.setPassword("151425");
        user5.setName("王振琦");
        user5.setRole("ROLE_USER,ROLE_PRO_OPE_EMPLOYEE");
        userRepository.save(user5);
    }
}
