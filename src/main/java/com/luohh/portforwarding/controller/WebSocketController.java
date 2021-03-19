package com.luohh.portforwarding.controller;


import com.luohh.actuator.entity.ClassLoadEntity;
import com.luohh.actuator.entity.GcEntity;
import com.luohh.actuator.entity.ThreadEntity;
import com.luohh.portforwarding.service.ClassService;
import com.luohh.portforwarding.service.GcService;
import com.luohh.portforwarding.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * @author luohuihua
 */
@Controller
public class WebSocketController {
    @Autowired
    private GcService gcService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ThreadService threadService;

    /**
     * gc 内存信息
     * MessageMapping注解和我们之前使用的@RequestMapping类似  前端主动发送消息到后台时的地址
     * SendTo注解表示当服务器有消息需要推送的时候，会对订阅了@SendTo中路径的浏览器发送消息。
     *
     * @return
     */
    @MessageMapping("/gc")
    @SendTo("/topic/gc")
    public List<GcEntity> socketGc() {
        return gcService.getAllGc();
    }

    /**
     * 类加载相关信息
     *
     * @return
     */
    @MessageMapping("/cl")
    @SendTo("/topic/cl")
    public List<ClassLoadEntity> socketCl() {
        return classService.getAllClassLoad();
    }

    /**
     * 线程相关信息
     *
     * @return
     */
    @MessageMapping("/thread")
    @SendTo("/topic/thread")
    public List<ThreadEntity> socketThread() {
        return threadService.getAllThread();
    }


    /**
     * 日志
     *
     * @return
     */
    @MessageMapping("/weblog")
    @SendTo("/topic/weblog")
    public String weblog(String url) {
//        JSONObject info = JSONObject.parseObject(HttpUtil.URLGet(url + "/actuator/info/logReader"));
//        if (info.containsKey("result")) {
//            System.err.println(info.getString("result"));
//            return info.getString("result");
//        } else {
        return "没有最新日志.......................";
//        }
    }
}
