package com.ssafy.ssafsound.global.docs.snippet;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import java.util.Map;

public class CookieDescriptionSnippet extends TemplatedSnippet {

    private CookieDescriptionSnippet(Map<String, Object> attributes) {
        super("cookie", attributes);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        return operation.getAttributes();
    }

    public static CookieDescriptionSnippet requestCookieAccessTokenNeedless() {

        return new CookieDescriptionSnippet(Map.ofEntries(
                Map.entry("cookie", "accessToken"),
                Map.entry("description", "액세스 토큰 불필요")
        ));
    }

    public static CookieDescriptionSnippet requestCookieAccessTokenMandatory() {

        return new CookieDescriptionSnippet(Map.ofEntries(
                Map.entry("cookie", "accessToken"),
                Map.entry("description", "액세스 토큰 필수")
        ));
    }

    public static CookieDescriptionSnippet requestCookieAccessTokenOptional() {

        return new CookieDescriptionSnippet(Map.ofEntries(
                Map.entry("cookie", "accessToken"),
                Map.entry("description", "액세스 토큰 옵션(쿠키 유무에 따라 다른 응답을 반환합니다.)")
        ));
    }
}
