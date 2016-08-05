package com.bg.controller;

/**
 * Created by Administrator on 2016/8/3.
 */

import com.bg.model.HostHolder;
import com.bg.model.Message;
import com.bg.model.User;
import com.bg.model.ViewObject;
import com.bg.service.MessageService;
import com.bg.service.UserService;
import com.bg.util.WatchMovieUtil;
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

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String msgContent) {
        try {
            //html 过滤
            Message msg = new Message();
            msg.setContent(msgContent);
            msg.setCreatedDate(new Date());
            msg.setToId(toId);
            msg.setFromId(fromId);
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return WatchMovieUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("add message failed" + e.getMessage());
            return WatchMovieUtil.getJSONString(1, "add message failed");
        }
    }

   
}