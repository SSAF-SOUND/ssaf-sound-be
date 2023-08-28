package com.ssafy.ssafsound.domain.auth.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientUtils {

    private static final List<String> headerNames = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );
    public static String getRemoteAddress() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        return headerNames.stream()
                .map(request::getHeader)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    public static String getClientDevice() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String agentName = request.getHeader("User-Agent");

        return Optional.ofNullable(agentName)
                .map(agent -> {
                    if (agent.contains("Safari")) return "Safari";
                    if (agent.contains("Chrome")) return "Chrome";
                    if (agent.contains("Firefox")) return "Firefox";
                    if (agent.contains("iPhone") && agent.contains("Mobile")) return "iPhone";
                    if (agent.contains("Android") && agent.contains("Mobile")) return "Android";
                    return "ETC";
                })
                .orElse("ETC");
    }
}
