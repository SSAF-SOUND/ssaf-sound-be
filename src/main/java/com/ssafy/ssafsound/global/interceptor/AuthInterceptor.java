package com.ssafy.ssafsound.global.interceptor;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String COOKIE = "Set-Cookie";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        if (isGetMethodWithRecruitUri(request) || isGetMethodWithAuthUri(request)
                || isGetMethodWithCommentsUri(request) || isGetMethodWithLunchPollUri(request) || isGetMethodWithMetaUri(request)
                || isGetMethodWithMembersUri(request) || isGetMethodWithPostsUri(request)) {
            return true;
        }

        if (isNotExistCookie(request)) {
            throw new AuthException(MemberErrorInfo.AUTH_TOKEN_NOT_FOUND);
        }

        /**
         * 차 후, 이 곳에 리프레시 토큰 재발급 요청이 왔으면 리프레시 토큰 검증로직 추가 예정
         */

        String token =  AuthorizationExtractor.extractToken("accessToken", request);

        if(isInValidToken(token)) {
            return false;
        }
        return true;
    }

    private boolean isGetMethodWithRecruitUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/recruits") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithAuthUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/auth") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithCommentsUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/comments") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithLunchPollUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/lunch") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithMetaUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/meta") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithMembersUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/members") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isGetMethodWithPostsUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/posts") && request.getMethod().equalsIgnoreCase("GET");
    }

    private boolean isNotExistCookie(HttpServletRequest request) {
        Enumeration<String> cookies = request.getHeaders(COOKIE);
        return !cookies.hasMoreElements();
    }

    private boolean isInValidToken(String token) {
        return !jwtTokenProvider.isValid(token);
    }
}
