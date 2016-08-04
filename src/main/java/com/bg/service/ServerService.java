package com.bg.service;

import com.bg.dao.ServerDAO;
import com.bg.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
@Service
public class ServerService {

    @Autowired
    ServerDAO serverDAO;

    public int addServer(Server server) {
        return serverDAO.addServer(server);
    }
    public List<Server> getServerList(int offset, int limit) {
        return serverDAO.getServerList(offset,limit);
    }
}
