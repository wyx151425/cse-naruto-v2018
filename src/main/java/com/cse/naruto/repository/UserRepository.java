package com.cse.naruto.repository;

import com.cse.naruto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户数据DAO层
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据用户名获取用户对象
     *
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
}
