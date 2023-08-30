package com.ssafy.ssafsound.domain.auth.service;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
@NoArgsConstructor
public class CookieProvider {

    public ResponseCookie setCookieWithOptions(String name, String value) {
        ResponseCookie responseCookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .maxAge(2629744)
            .secure(true)
            .path("/")
            .sameSite("None")
            .build();
        return responseCookie;
    }

    public void setResponseWithCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie, refreshTokenCookie;
        if (accessToken == null && refreshToken == null) {
            accessTokenCookie = deleteCookie("accessToken", null);
            refreshTokenCookie = deleteCookie("refreshToken", null);
        } else {
            accessTokenCookie = setCookieWithOptions("accessToken", accessToken);
            refreshTokenCookie = setCookieWithOptions("refreshToken", refreshToken);
        }
        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public ResponseCookie deleteCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .maxAge(0)
                .secure(true)
                .path("/")
                .build();
        return cookie;
    }
}
