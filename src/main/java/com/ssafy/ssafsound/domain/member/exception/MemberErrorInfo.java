package com.ssafy.ssafsound.domain.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorInfo {

    MEMBER_ROLE_TYPE_NOT_FOUND("703", "멤버 권한을 찾을 수 없는 문제가 발생했습니다."),
    MEMBER_NOT_FOUND_BY_ID("704", "멤버를 찾을 수 없습니다."),
    MEMBER_TOKEN_NOT_FOUND("708", "멤버 ID로 토큰을 찾을 수 없습니다."),
    MEMBER_OAUTH_NOT_FOUND("709", "소셜 로그인에 문제가 발생했습니다.");

    private final String code;
    private final String message;

    MemberErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
