package com.bg.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.bg.controller.MessageController;
import com.bg.model.*;
import com.bg.service.MessageService;
import com.bg.service.ServerService;
import com.bg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */

@Controller("adminHomeController")
public class HomeController {

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    ServerService serverService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(path = {"/admin/", "/admin/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        try {
            int userId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(userId, 0, 10);

            Date date = new Date();
            //System.out.println(date);
            for (Message msg : conversationList) {
                //System.out.println(msg.getContent());
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                List<Message> detailList = messageService.getConversationDetail(msg.getConversationId(),0,10);
                List<ViewObject> messages = new ArrayList<>();
                for (Message msgg : detailList){
                    ViewObject vo2 = new ViewObject();
                    //System.out.println(msg.getId());
                    messageService.readMessage(msgg.getId());
                    vo2.set("message",msgg);
                    User user = userService.getUser(msgg.getFromId());
                    if(user == null){
                        continue;
                    }
                    vo2.set("headUrl",user.getHeadUrl());
                    vo2.set("userId",user.getId());
                    vo2.set("userName",user.getName());
                    messages.add(vo2);
                }
                model.addAttribute("messages",messages);
                //System.out.println(msg.getCreatedDate());
                vo.set("time",String.valueOf((date.getTime() - msg.getCreatedDate().getTime())/(60*1000)));
                vo.set("unread", messageService.getConversationUnReadCount(userId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            model.addAttribute("localUserId", userId);
        } catch (Exception e) {
            logger.error("Read message list failed" + e.getMessage());
        }
        return "pages/index";
    }

    @RequestMapping(path = {"/admin/dispatch"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String upload(Model model) {
        List<Server> servers = serverService.getServerList(0, 10);
        model.addAttribute("servers",servers);
        return "pages/dispatch";
    }

    @RequestMapping(path = {"/admin/loginPage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String login() {
        return "pages/login";
    }

}

