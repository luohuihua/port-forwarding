package com.luohh.portforwarding.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luohuihua
 */
public class IpUtil {

    public static String getIp() {
        ServletRequestAttributes requestAttributes = ServletRequestAttributes.class.
                cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest contextRequest = requestAttributes.getRequest();
        return getIp(contextRequest);
    }

    public static String getIp(HttpServletRequest request) {
        //配置代理情况，配置代理后header信息通过x-forwarded-for标记真实ip地址
        String ip = request.getHeader("x-forwarded-for");

        //整合apache+Weblogic 代理情况
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        //整合apache+Weblogic 代理情况
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        //获取未配置代理的情况
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }

        ip = getIp(ip);
        return ip;
    }

    private static String getIp(String ip) {
        if ((ip != null) && (ip.indexOf(44) > 0)) {
            String[] ipArray = ip.split(",");
            ip = ipArray[(ipArray.length - 1)].trim();
        }
        return ip;
    }
}
