package com.ssafy.ssafsound.domain.auth.service;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@NoArgsConstructor
public class CookieProvider {

    public ResponseCookie setRefreshTokenCookie(String value) {
        return ResponseCookie.from("refreshToken", value)
            .httpOnly(true)
            .maxAge(2629744)
            .secure(true)
            .path("/auth/reissue")
            .build();
    }

    public ResponseCookie setAccessTokenCookie(String value) {
        return ResponseCookie.from("accessToken", value)
            .httpOnly(true)
            .maxAge(2629744)
            .secure(true)
            .path("/")
            .build();
    }

    public void setResponseWithCookies(HttpServletResponse response, String accessToken, String refreshToken) {

        if (accessToken == null && refreshToken == null) {
            response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie("accessToken", null).toString());
            response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie("refreshToken", null).toString());
        } else {
            response.addHeader(HttpHeaders.SET_COOKIE, setAccessTokenCookie(accessToken).toString());
            response.addHeader(HttpHeaders.SET_COOKIE, setRefreshTokenCookie(refreshToken).toString());
        }
    }

    public ResponseCookie deleteCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .maxAge(0)
                .secure(true)
                .path("/")
                .build();
    }
}
