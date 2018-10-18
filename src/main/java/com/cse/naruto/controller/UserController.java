package com.cse.naruto.controller;

import com.cse.naruto.model.Response;
import com.cse.naruto.model.User;
import com.cse.naruto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户数据请求控制器
 *
 * @author 王振琦
 * createAt 2018/09/20
 * updateAt 2018/09/20
 */
@RestController
@RequestMapping("api/users")
public class UserController extends NarutoFacade {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "login")
    public Response<User> actionLogin(@RequestBody User requestUser) {
        User user = userService.login(requestUser);
        addCurrentUser(user);
        return new Response<>(user);
    }
}
