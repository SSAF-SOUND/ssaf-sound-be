package com.ssafy.ssafsound.domain.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorInfo {
    AUTH_VALUE_NOT_FOUND("700", "code값에 문제가 있거나 Oauth 이름이 잘못되었습니다."),
    AUTH_SERVER_ERROR("701", "서버에서 소셜 로그인을 제공하는데 문제가 발생했습니다."),
    AUTH_SERVER_PARSING_ERROR("702", "서버에서 파싱하는데 문제가 발생했습니다."),
    AUTH_TOKEN_INVALID("705", "토큰이 유효하지 않습니다."),
    AUTH_TOKEN_EXPIRED("706", "토큰이 만료됐습니다."),
    AUTH_TOKEN_SERVICE_ERROR("707", "서버에서 토큰을 처리하는 과정에서 문제가 발생했습니다.");

    private final String code;
    private final String message;

    AuthErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
