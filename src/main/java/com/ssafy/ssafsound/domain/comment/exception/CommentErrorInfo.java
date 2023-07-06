package com.ssafy.ssafsound.domain.comment.exception;

import lombok.Getter;

@Getter
public enum CommentErrorInfo {
    NOT_FOUND_COMMENT("806", "댓글을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    CommentErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
