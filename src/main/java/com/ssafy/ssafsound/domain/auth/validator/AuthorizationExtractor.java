package com.ssafy.ssafsound.domain.auth.validator;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@NoArgsConstructor
public class AuthorizationExtractor {

    public static String extractToken(String tokeType, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokeType)) {
                return cookie.getValue();
            }
        }
        throw new AuthException(MemberErrorInfo.AUTH_TOKEN_NOT_FOUND);
    }
}
