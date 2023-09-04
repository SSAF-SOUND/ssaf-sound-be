package com.ssafy.ssafsound.domain.auth.service;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
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
        List<String> cookieStrings = new ArrayList<>();

        if (accessToken == null && refreshToken == null) {
            cookieStrings.add(deleteCookie("accessToken", null).toString());
            cookieStrings.add(deleteCookie("refreshToken", null).toString());
        } else {
            cookieStrings.add(setAccessTokenCookie(accessToken).toString());
            cookieStrings.add(setRefreshTokenCookie(refreshToken).toString());
        }

        response.setHeader(HttpHeaders.SET_COOKIE, String.join("; ", cookieStrings));
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
