package com.ssafy.ssafsound.domain.auth.exception;

import lombok.Getter;

@Getter
public enum MemberErrorInfo {
    AUTH_VALUE_NOT_FOUND("700", "code값에 문제가 있거나 Oauth 이름이 잘못되었습니다."),
    AUTH_SERVER_ERROR("701", "서버에서 소셜 로그인을 제공하는데 문제가 발생했습니다."),
    AUTH_SERVER_PARSING_ERROR("702", "서버에서 파싱하는데 문제가 발생했습니다."),
    MEMBER_ROLE_TYPE_NOT_FOUND("703", "멤버 권한을 찾을 수 없는 문제가 발생했습니다."),
    MEMBER_NOT_FOUND_BY_ID("704", "멤버를 찾을 수 없습니다."),
    AUTH_TOKEN_NOT_FOUND("705", "토큰이 유효하지 않습니다."),
    AUTH_TOKEN_TIME_OUT("706", "토큰이 만료됐습니다.");
    private String code;
    private String message;

    MemberErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
