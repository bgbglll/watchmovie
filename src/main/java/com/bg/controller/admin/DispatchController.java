package com.bg.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.async.EventModel;
import com.bg.async.EventProducer;
import com.bg.async.EventType;
import com.bg.model.EntityType;
import com.bg.model.HostHolder;
import com.bg.model.fileclient.RequestFile;
import com.bg.model.fileclient.ResponseFile;
import com.bg.service.fileclient.FileTransferClient;
import com.bg.util.JedisAdapter;
import com.bg.util.MD5FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/8/1.
 */
@Controller
public class DispatchController {

    private static final Logger logger = LoggerFactory.getLogger(DispatchController.class);

    private final Executor exec = Executors.newCachedThreadPool();

    private Map<String, String> md5ToFileName = new HashMap<>();

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/transferFile/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String transferFile(@RequestParam("qqfile") List<MultipartFile> fileTmp
            , @RequestParam("ip") String ipList
            , @RequestParam("port") String portList) {
        try {
            String[] ip = ipList.split(" ");
            String[] port = portList.split(" ");

            String fName = "";
            List<String> md5 = new ArrayList<>();

            if (ip.length != port.length) {
                logger.error("数组长度不一致");
                return "error";
            }

            int len = ip.length;
            for (MultipartFile fTmp : fileTmp) {
                RequestFile echo = new RequestFile();
                //System.out.println(fileTmp.getName());
                //System.out.println(fTmp.getOriginalFilename());
                fName = fTmp.getOriginalFilename();
                File file = new File("D:/deaProjects/netty-web-filestransfer/tmp/" + fTmp.getOriginalFilename());  //  "D://files/xxoo"+args[0]+".amr"
                if (!file.exists())
                    fTmp.transferTo(file);


                String fileName = file.getName();// 文件名
                echo.setFile(file);
                String m = MD5FileUtil.getFileMD5String(file);
                echo.setFile_md5(m);
                md5.add(m);
                echo.setFile_name(fileName);
                md5ToFileName.put(m, fileName);

                echo.setFile_type(FileTransferClient.getSuffix(fileName));
                echo.setStarPos(0);// 文件开始位置
                //System.out.println(echo);


                for (int i = 0; i < len; i++) {
                    int p = Integer.parseInt(port[i]);
                    String _ip = ip[i];
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            try {

                                new FileTransferClient().connect(p, _ip, echo);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    exec.execute(task);
                }
            }

            JSONObject json = new JSONObject();
            json.put("success", "ok");
            json.put("md5", md5);
            //json.put("number",String.valueOf(len));
            json.put("fileName", fName);
            //System.out.println(json.toJSONString());
            return json.toJSONString();
        } catch (Exception e) {
            logger.error("发送文件失败" + e.getMessage());
            return "发送文件失败";
        }

    }


    @RequestMapping(path = {"/getProgress"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getProgress() {
        JSONObject json = new JSONObject();

        String response = jedisAdapter.rpop("response");

        if (response == null) {

            return null;
        }
        //System.out.println(response);

        ResponseFile resfile = JSON.parseObject(response, ResponseFile.class);

        //asyn
        if (resfile.getProgress() == 100) {
            json.put("done", "1");
            System.out.println("done");
            eventProducer.fireEvent(new EventModel(EventType.DISPATCH)
                    .setEntityOwnerId(hostHolder.getUser().getId())
                    // system id;
                    .setActorId(1)
                    .setEntityId(0)
                    .setEntityType(EntityType.ENTITY_DISPATCH)
                    .setContent("In " + resfile.getServerName() + ", File: " + md5ToFileName.get(resfile.getFile_md5())));
        }
        else json.put("done","0");

        json.put("progress", String.valueOf((resfile.getProgress())));
        json.put("id", resfile.getFile_md5());
        //System.out.println(resfile.getServerName());
        json.put("server", resfile.getServerName());


    //System.out.println("in");


        return json.toJSONString();
}

}
