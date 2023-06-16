package com.ssafy.ssafsound.global.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorInfo {
    INTERNAL_SERVER_ERROR("500","Internal Server Error"),
    NOT_FOUND("404","Resource Not Found Error"),
    AUTH_VALUE_NOT_FOUND("700", "code값에 문제가 있거나 Oauth 이름이 잘못되었습니다."),
    AUTH_SERVER_ERROR("701", "서버에서 소셜 로그인을 제공하는데 문제가 발생했습니다.");

    private String code;
    private String message;

    GlobalErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}