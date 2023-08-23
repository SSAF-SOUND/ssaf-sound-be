package com.ssafy.ssafsound.domain.auth.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

public class ClientUtils {
    public static String getRemoteAddress() {
        String remoteAddress = null;
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();

        remoteAddress = request.getHeader("X-Forwarded-For");
        if (isNull(remoteAddress)) {
            remoteAddress = request.getHeader("Proxy-Client-IP");
        }
        if (isNull(remoteAddress)) {
            remoteAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isNull(remoteAddress)) {
            remoteAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isNull(remoteAddress)) {
            remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isNull(remoteAddress)) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    public static String getClientDevice() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        String agent = request.getHeader("User-Agent");
        String browser = "ETC";

        if (!isNull(agent)) {
            if (agent.contains("Safari")) {
                browser = "Safari";
            } else if (agent.contains("Chrome")) {
                browser = "Chrome";
            } else if (agent.contains("Firefox")) {
                browser = "Firefox";
            } else if (agent.contains("iPhone") && agent.contains("Mobile")) {
                browser = "iPhone";
            } else if (agent.contains("Android") && agent.contains("Mobile")) {
                browser = "Android";
            }
        }
        return browser;
    }
}
