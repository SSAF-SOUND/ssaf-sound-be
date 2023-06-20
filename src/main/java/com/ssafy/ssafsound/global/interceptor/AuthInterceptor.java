package com.ssafy.ssafsound.global.interceptor;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String COOKIE = "Set-Cookie";

    private final List<String> excludePathByClientGetRequest = List.of("/auth", "/recruits", "/comments", "/lunch", "/meta", "/members", "/posts");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        if (isExcludeGetRequest(request)) {
            return true;
        }

        if (isNotExistCookie(request)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        /**
         * 차 후, 이 곳에 리프레시 토큰 재발급 요청이 왔으면 리프레시 토큰 검증로직 추가 예정
         */

        String token =  AuthorizationExtractor.extractToken("accessToken", request);
        if(isInvalidToken(token)) {
            throw new AuthException(MemberErrorInfo.AUTH_TOKEN_NOT_FOUND);
        }
        return true;
    }

    private boolean isNotExistCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Objects.isNull(cookies);
    }

    private boolean isInvalidToken(String token) {
        return !jwtTokenProvider.isValid(token);
    }

    public boolean isExcludeGetRequest(HttpServletRequest request) {
        String url = excludePathByClientGetRequest.stream().filter(path->request.getRequestURI().contains(path)).findFirst().orElse(null);
        return url != null && request.getMethod().equalsIgnoreCase("GET");
    }
}
