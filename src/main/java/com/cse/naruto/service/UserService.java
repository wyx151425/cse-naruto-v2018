package com.cse.naruto.service;

import com.cse.naruto.model.User;

/**
 * 用户业务逻辑
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param user 封装前端数据的用户对象
     * @return 具有完整信息的用户对象
     */
    User login(User user);

    /**
     * 用户注册
     *
     * @param user 封装前端数据的用户对象
     * @return 具有完整信息的用户对象
     */
    User register(User user);
}
