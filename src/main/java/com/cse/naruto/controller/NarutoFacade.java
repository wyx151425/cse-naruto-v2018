package com.cse.naruto.controller;

import com.cse.naruto.model.User;
import com.cse.naruto.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 控制器原型类
 *
 * @author 王振琦
 * createAt 2018/09/19
 * updateAt 2018/09/19
 */
public class NarutoFacade {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpSession session;

    HttpServletRequest getRequest() {
        return request;
    }

    HttpServletResponse getResponse() {
        return response;
    }

    HttpSession getSession() {
        return session;
    }

    void addCurrentUser(User user) {
        session.setAttribute(Constant.USER, user);
    }

    User getCurrentUser() {
        return (User) session.getAttribute(Constant.USER);
    }

    void removeCurrentUser() {
        session.setAttribute(Constant.USER, null);
    }
}
