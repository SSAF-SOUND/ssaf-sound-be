package com.ssafy.ssafsound.domain.auth.validator;

import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor
public class AuthorizationExtractor {

    public static final String BEARER_TYPE = "Bearer";

    public static String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        return extract(headers);
    }

    private static String extract(Enumeration<String> headers) {
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();

                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
