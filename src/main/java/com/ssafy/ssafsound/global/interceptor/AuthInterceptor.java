package com.ssafy.ssafsound.global.interceptor;

import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

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

        if (isNotExistHeader(request)) {
            return false;
        }

        String token =  AuthorizationExtractor.extractAccessToken(request);

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

    private boolean isNotExistHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Objects.isNull(authorizationHeader);
    }

    private boolean isInValidToken(String token) {
        return !jwtTokenProvider.isValid(token);
    }
}
