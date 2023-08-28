package com.ssafy.ssafsound.domain.auth.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
@NoArgsConstructor
public class CookieProvider {

    public Cookie setCookieWithOptions(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setDomain(".ssafsound.com");
        cookie.setMaxAge(2629744);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    public void setResponseWithCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessTokenCookie, refreshTokenCookie;
        if (accessToken == null && refreshToken == null) {
            accessTokenCookie = deleteCookie("accessToken", null);
            refreshTokenCookie = deleteCookie("refreshToken", null);
        } else {
            accessTokenCookie = setCookieWithOptions("accessToken", accessToken);
            refreshTokenCookie = setCookieWithOptions("refreshToken", refreshToken);
        }
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    public Cookie deleteCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }
}
