package com.ssafy.ssafsound.domain.post.exception;

import lombok.Getter;

@Getter
public enum PostErrorInfo {
    NO_BOARD_ID("801", "존재하지 않는 게시글 입니다.");

    private final String code;
    private final String message;

    PostErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
