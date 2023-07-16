package com.ssafy.ssafsound.domain.auth.validator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@NoArgsConstructor
public class AuthorizationExtractor {

    public static String extractToken(String tokenType, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String bearHeader = null;
        // cookie 없는 경우(미인증)
        if (cookies == null) {
            bearHeader = request.getHeader("Authorization");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenType)) {
                return cookie.getValue();
            }
        }

        bearHeader = request.getHeader("Authorization");
        if(bearHeader!=null) {
            String[] headerToken = bearHeader.split(" ");
            bearHeader = headerToken[1];
        }
        return bearHeader;
    }
}
