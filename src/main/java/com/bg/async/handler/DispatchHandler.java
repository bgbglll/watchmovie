package com.bg.async.handler;

import com.bg.async.EventHandler;
import com.bg.async.EventModel;
import com.bg.async.EventType;
import com.bg.model.Message;
import com.bg.model.User;
import com.bg.service.MessageService;
import com.bg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
@Component
public class DispatchHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        //System.out.println("Liked");
        Message message = new Message();
        //“1”：system account id
        int fromId = 1;
        int toId = model.getEntityOwnerId();
        message.setFromId(fromId);
        message.setToId(toId);

        String content = "Dispatch is over!";
        message.setContent(content);
        //message.setContent("<a href='" + userUrl + "'>" + "用户" + user.getName() + "</a>" + "攒了您的咨询," + "<a href='" + newsUrl + "'>" + newsUrl + "</a>");
        message.setCreatedDate(new Date());

//      message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.DISPATCH);
    }
}
