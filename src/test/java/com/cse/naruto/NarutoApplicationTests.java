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
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        permission.setCreateAt(dateTime);
        permission.setUpdateAt(dateTime);
        return permission;
    }

    private User createUser() {
        User user = new User();
        user.setObjectId(Generator.getObjectId());
        user.setStatus(Constant.UserStatus.COMMON);
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
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
        permission1.setName("MY_NAME");
        permissionList.add(permission1);

        Permission permission2 = createPermission();
        permission2.setRole("ROLE_USER");
        permission2.setName("MY_EYE");
        permissionList.add(permission2);

        permissionRepository.saveAll(permissionList);
    }

    @Test
    public void addUser() {
        User user = createUser();
        user.setUsername("wyx151425");
        user.setPassword("151425");
        user.setName("王振琦");
        user.setRoles("ROLE_USER,");
        userRepository.save(user);
    }
}
