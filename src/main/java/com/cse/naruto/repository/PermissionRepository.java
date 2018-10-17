package com.cse.naruto.repository;

import com.cse.naruto.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 权限数据DAO层
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    /**
     * 根据角色名称获取对应权限
     * @param role 角色名称
     * @return 权限对象集合
     */
    List<Permission> findAllByRole(String role);
}
