package com.bg.configuration;


import com.bg.interceptor.AdminLoginRequiredInterceptor;
import com.bg.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2016/7/6.
 */
@Component
public class WatchMovieWebConfiguration extends WebMvcConfigurerAdapter{


    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    AdminLoginRequiredInterceptor adminLoginRequiredInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(adminLoginRequiredInterceptor)
                .excludePathPatterns("/admin/loginPage")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/reg");


        super.addInterceptors(registry);
    }
}
