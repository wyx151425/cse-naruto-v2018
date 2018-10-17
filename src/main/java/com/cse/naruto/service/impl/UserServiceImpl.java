package com.cse.naruto.service.impl;

import com.cse.naruto.context.exception.NarutoException;
import com.cse.naruto.model.Permission;
import com.cse.naruto.model.User;
import com.cse.naruto.repository.PermissionRepository;
import com.cse.naruto.repository.UserRepository;
import com.cse.naruto.service.UserService;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.Generator;
import com.cse.naruto.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户业务逻辑实现类
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public User login(User user) {
        User targetUser = userRepository.findByUsername(user.getUsername());
        if (null == targetUser) {
            throw new NarutoException(StatusCode.USER_NOT_EXIST);
        } else {
            if (0 == targetUser.getStatus()) {
                throw new NarutoException(StatusCode.USER_DISABLED);
            } else {
                if (targetUser.getPassword().equals(user.getPassword())) {
                    // 根据用户角色获取用户权限
                    String[] roles = targetUser.getRoles().split(",");
                    Map<String, Boolean> permissions = new HashMap<>();
                    for (String role : roles) {
                        List<Permission> permissionList = permissionRepository.findAllByRole(role);
                        for (Permission permission : permissionList) {
                            permissions.put(permission.getName(), true);
                        }
                    }
                    targetUser.setPermissions(permissions);
                    return targetUser;
                } else {
                    throw new NarutoException(StatusCode.USER_LOGIN_PASSWORD_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        User targetUser = userRepository.findByUsername(user.getUsername());
        if (null == targetUser) {
            user.setObjectId(Generator.getObjectId());
            LocalDateTime dateTime = LocalDateTime.now().withNano(0);
            user.setCreateAt(dateTime);
            user.setUpdateAt(dateTime);
            user.setRoles(Constant.UserRoles.ROLE_USER);
            userRepository.save(user);
            return user;
        } else {
            throw new NarutoException(StatusCode.USER_REGISTERED);
        }
    }
}
