package com.ssafy.ssafsound.domain.auth.validator;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@NoArgsConstructor
public class AuthorizationExtractor {
    private static final String COOKIE = "Set-Cookie";

    public static String extractToken(String tokeType, HttpServletRequest request) {
        Enumeration<String> cookies = request.getHeaders(COOKIE);
        while (cookies.hasMoreElements()) {
            String cookieInfo = cookies.nextElement();
            Pattern regex = Pattern.compile(tokeType + "=" + "([^;]+)");
            Matcher matcher = regex.matcher(cookieInfo);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        throw new AuthException(MemberErrorInfo.AUTH_TOKEN_NOT_FOUND);
    }
}
