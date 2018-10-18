package com.cse.naruto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 路由控制器
 *
 * @author 王振琦
 * createAt 2018/10/18
 * updateAt 2018/10/18
 */
@Controller
public class RouteController {

    @RequestMapping(value = {"", "/", "index"})
    public String routeIndexPage() {
        return "index";
    }

    @RequestMapping(value = "login")
    public String routeLoginPage() {
        return "login";
    }

    @RequestMapping(value = "material/edit")
    public String routeMaterialEditPage() {
        return "material-edit";
    }
}
