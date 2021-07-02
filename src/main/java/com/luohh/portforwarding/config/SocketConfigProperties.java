package com.luohh.portforwarding.config;

import com.luohh.portforwarding.model.IpWhiteList;
import com.luohh.portforwarding.model.PortMapperAddress;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * socket相关配置
 *
 * @author luohuihua
 */
@Data
@Component
@ConfigurationProperties(prefix = "socket")
public class SocketConfigProperties {
    /**
     * 端口
     */
    private Integer port;
    /**
     * 超时时间
     */
    private Long timeOut;
    /**
     * 目标地址
     */
    private List<PortMapperAddress> portMapperAddress;
    /**
     * ip白名单
     */
    private List<IpWhiteList> ipWhiteLists;
    /**
     * 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁
     */
    private Long poolKeepAliveTime;
    /**
     * 核心线程数
     */
    private Integer poolCore;
    /**
     * 最大线程数
     */
    private Integer poolMax;
    /**
     * 线程队列容量
     */
    private Integer poolQueueInit;
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;
}
