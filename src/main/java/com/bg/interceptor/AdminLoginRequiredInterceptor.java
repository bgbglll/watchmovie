package com.bg.interceptor;

import com.bg.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/7/6.
 */
@Component
public class AdminLoginRequiredInterceptor implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //System.out.println(hostHolder.getUser().getType().equals("admin"));
        //System.out.println(hostHolder.getUser() == null);
//        if(hostHolder.getUser() != null){
//            System.out.println(hostHolder.getUser().getType());
//        }
        if(hostHolder.getUser() == null || !hostHolder.getUser().getType().equals("admin")){
            httpServletResponse.sendRedirect("/admin/loginPage");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
