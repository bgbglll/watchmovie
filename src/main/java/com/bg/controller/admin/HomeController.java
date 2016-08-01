package com.bg.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2016/8/1.
 */

@Controller("adminHomeController")
public class HomeController {

    @RequestMapping(path = {"/admin/", "/admin/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index() {
        return "pages/index";
    }

    @RequestMapping(path = {"/admin/dispatch"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String upload() {
        return "pages/dispatch";
    }

    @RequestMapping(path = {"/admin/loginPage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String login() {
        return "pages/login";
    }
}
