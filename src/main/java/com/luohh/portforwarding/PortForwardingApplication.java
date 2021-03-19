package com.luohh.portforwarding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动类
 *
 * @author luohuihua
 */
@SpringBootApplication
@Slf4j
public class PortForwardingApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(PortForwardingApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "端口转发服务正在运行！访问URL:\n\t" +
                "本地: http://localhost:" + port + path + "/\n\t" +
                "外部: http://" + ip + ":" + port + path + "/\n\t" +
                "----------------------------------------------------------");
    }
}
