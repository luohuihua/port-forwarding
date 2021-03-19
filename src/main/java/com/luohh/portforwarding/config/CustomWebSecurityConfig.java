package com.luohh.portforwarding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 解决websocket跨域问题
 *
 * @author luohuihua
 */
@Configuration
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/websocket/**");
    }
}
