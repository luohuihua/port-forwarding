package com.luohh.portforwarding.controller;

import com.luohh.actuator.jvm.Jmap;
import com.luohh.actuator.jvm.Jstack;
import com.luohh.actuator.jvm.Server;
import com.luohh.portforwarding.config.SocketConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author luohuihua
 */
@Controller
@Slf4j
public class BaseController {
    @Autowired
    private SocketConfigProperties socketConfigProperties;

    /**
     * 首页
     *
     * @param mmap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/")
    public String main(ModelMap mmap) throws Exception {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        ThreadPoolExecutor threadPoolExecutor = socketConfigProperties.getThreadPoolExecutor();
        // 线程任务总数
        mmap.put("taskSum", threadPoolExecutor.getTaskCount());
        // 线程任务运行数
        mmap.put("taskRunningSum", threadPoolExecutor.getActiveCount());
        // 线程任务空闲数
        mmap.put("taskFreeSum", threadPoolExecutor.getCompletedTaskCount());
        return "index";
    }

    @RequestMapping(value = "/monitor")
    public String monitor(ModelMap mmap) throws Exception {
        return "monitor";
    }

    @RequestMapping("/heap")
    public ResponseEntity<byte[]> heapDump() throws IOException {
        String dump = Jmap.dump();
        File file = new File(dump);
        log.info("DownLoad Dump:" + dump);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    @RequestMapping("/thread")
    public ResponseEntity<byte[]> threadDump() throws IOException {
        String dump = Jstack.dump();
        File file = new File(dump);
        log.info("DownLoad Dump:" + dump);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
