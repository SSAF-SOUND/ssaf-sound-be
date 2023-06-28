package com.ssafy.ssafsound.domain.auth.validator;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@NoArgsConstructor
public class AuthorizationExtractor {

    public static String extractToken(String tokeType, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // cookie 없는 경우(미인증)
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokeType)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
