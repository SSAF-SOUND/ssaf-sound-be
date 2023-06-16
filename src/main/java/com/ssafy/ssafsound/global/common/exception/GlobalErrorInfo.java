package com.ssafy.ssafsound.global.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorInfo {
    INTERNAL_SERVER_ERROR("500","Internal Server Error"),
    NOT_FOUND("404","Resource Not Found Error"),
    AUTH_VALUE_NOT_FOUND("700", "Auth Values Code or Auth Name Not Found Error");

    private String code;
    private String message;

    GlobalErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}