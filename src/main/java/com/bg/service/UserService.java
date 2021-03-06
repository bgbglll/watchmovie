package com.bg.service;


import com.bg.dao.LoginTicketDAO;
import com.bg.dao.UserDAO;
import com.bg.model.HostHolder;
import com.bg.model.LoginTicket;
import com.bg.model.User;
import com.bg.util.WatchMovieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/8/4.
 */
@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User getUser(String username) {
        return userDAO.selectByName(username);
    }

    public Map<String, Object> register(String username, String password, String type){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname", "username is empty");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "password is empty");
            return map;
        }

        User user = userDAO.selectByName(username);

        if(user != null){
            map.put("msgname", "username is registered");
            return map;
        }

        //Sign up
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WatchMovieUtil.MD5(password+user.getSalt()));
        //System.out.println(user.getName() + " " + user.getPassword());
        user.setType(type);
        userDAO.addUser(user);

        //Sign in auto
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);


        return map;
    }

    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname", "username is empty");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "password is empty");
            return map;
        }

        User user = userDAO.selectByName(username);

        if(user == null){
            map.put("msgname", "username does not exist");
            return map;
        }
        //System.out.println(password);
        //System.out.println(user.getSalt());
        //System.out.println(ToutiaoUtil.MD5(password + user.getSalt()));
        if(!WatchMovieUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd", "password is wrong");
            return map;
        }


        //Sign in
        map.put("userId", user.getId());
        //ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() +  1000*3600*24);
        ticket.setExpired(date);
        ticket.setLogin(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }


    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    public Date getLoginTime() {
        return loginTicketDAO.selectByUserId(hostHolder.getUser().getId()).get(0).getLogin();
    }
}
