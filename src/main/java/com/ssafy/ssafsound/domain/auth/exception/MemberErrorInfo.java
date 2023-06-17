package com.ssafy.ssafsound.domain.auth.exception;

import lombok.Getter;

@Getter
public enum MemberErrorInfo {
    AUTH_VALUE_NOT_FOUND("700", "code값에 문제가 있거나 Oauth 이름이 잘못되었습니다."),
    AUTH_SERVER_ERROR("701", "서버에서 소셜 로그인을 제공하는데 문제가 발생했습니다."),
    AUTH_SERVER_PARSING_ERROR("702", "서버에서 파싱하는데 문제가 발생했습니다.");

    private String code;
    private String message;

    MemberErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
