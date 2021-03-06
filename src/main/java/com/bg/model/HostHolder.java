package com.bg.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/8/3.
 */
@Component
public class HostHolder {
    private static  ThreadLocal<User> users = new ThreadLocal<>();
    public boolean isEmpty(){
        return users.get() == null;
    }
    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void cler(){
        users.remove();
    }
}
