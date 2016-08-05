package com.bg.controller.admin;

import com.bg.service.UserService;
import com.bg.util.WatchMovieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/2.
 */
@Controller("adminLoginController")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/admin/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("type") String type,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {

        try{
            Map<String, Object> map = userService.register(username,password,type);
            //System.out.println(username + " " + password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
            }
            return WatchMovieUtil.getJSONString(0,"注册成功");
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return WatchMovieUtil.getJSONString(1,"注册异常");
        }
    }
    @RequestMapping(path = {"/admin/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {

        try{
            Map<String, Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
            }
            return WatchMovieUtil.getJSONString(0,"登陆成功");
        }catch (Exception e){
            logger.error("登陆异常" + e.getMessage());
            return WatchMovieUtil.getJSONString(1,"登陆异常");
        }
    }

    @RequestMapping(path = {"/admin/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/admin";
    }
}
