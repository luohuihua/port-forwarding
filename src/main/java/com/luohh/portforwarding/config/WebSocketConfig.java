package com.luohh.portforwarding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * webscoket 配置
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,
 * 此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 *
 * @author luohuihua
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static long HEART_BEAT = 5000;

    /**
     * 配置消息代理(message broker)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();
        config.enableSimpleBroker("/topic").setHeartbeatValue(new long[]{HEART_BEAT, HEART_BEAT}).setTaskScheduler(te);
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 注册协议节点,并映射指定的URl
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/websocket").withSockJS();
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
    }
}