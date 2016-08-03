package com.bg;

import com.bg.dao.UserDAO;
import com.bg.model.User;
import com.bg.util.WatchMovieUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

/**
 * Created by Administrator on 2016/7/2.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WatchmovieApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    UserDAO userDAO;

    @Test
    public void initData() {
        Random random = new Random();
        User admin = new User();
        admin.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        admin.setName("admin");
        admin.setPassword(WatchMovieUtil.MD5("admin"));
        admin.setSalt("");
        admin.setType("admin");
        userDAO.addUser(admin);
    }
}
