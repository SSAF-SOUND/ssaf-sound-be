package com.ssafy.ssafsound.global.common.exception;

import lombok.Getter;

@Getter
public enum GlobalErrorInfo {
    INTERNAL_SERVER_ERROR("500","Internal Server Error");

    private String code;
    private String message;

    GlobalErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}