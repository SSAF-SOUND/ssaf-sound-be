package com.ssafy.ssafsound.domain.post.exception;

import lombok.Getter;

@Getter
public enum PostErrorInfo {
    NOT_FOUND("802", "게시글을 찾을 수 없습니다."),
    DUPLICATE_REPORT("803", "이미 신고된 게시글입니다."),
    UNAUTHORIZED_DELETE_POST("804", "해당 게시글 삭제 권한이 없습니다.");


    private final String code;
    private final String message;

    PostErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
