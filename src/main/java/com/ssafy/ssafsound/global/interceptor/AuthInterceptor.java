package com.ssafy.ssafsound.global.interceptor;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
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

    private final List<String> excludePathByClientGetRequest = List.of("/auth", "/recruits", "/comments", "/lunch", "/meta", "/members", "/posts");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isGetMethodWithReissue(request)) {
            String token = AuthorizationExtractor.extractToken("refreshToken", request);
            log.info("Auth intercept token value: {}", token);
            validToken(token);
            return true;
        }

        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        if (isExcludeGetRequest(request)) {
            return true;
        }

        // if (isNotExistCookie(request)) {
        //     response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //     return false;
        // }

        String token = AuthorizationExtractor.extractToken("accessToken", request);
        log.info("Auth intercept token value: {}", token);
        validToken(token);
        return true;
    }

    private boolean isNotExistCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Objects.isNull(cookies);
    }

    public boolean isExcludeGetRequest(HttpServletRequest request) {
        String url = excludePathByClientGetRequest.stream().filter(path->request.getRequestURI().contains(path)).findFirst().orElse(null);
        return url != null && request.getMethod().equalsIgnoreCase("GET");
    }

    public boolean isGetMethodWithReissue(HttpServletRequest request) {
        return request.getRequestURI().contains("/auth/reissue") && request.getMethod().equalsIgnoreCase("GET");
    }

    public void validToken(String token) {
        if(!jwtTokenProvider.isValid(token)) throw new AuthException(AuthErrorInfo.AUTH_TOKEN_EXPIRED);
    }
}
