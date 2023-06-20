package com.ssafy.ssafsound.domain.post.exception;

import lombok.Getter;

@Getter
public enum PostErrorInfo {
    INVALID_POST("802", "게시물을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    PostErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
