package com.ssafy.ssafsound.domain.post.exception;

import lombok.Getter;

@Getter
public enum PostErrorInfo {
    NOT_FOUND_POST("802", "게시글을 찾을 수 없습니다."),
    NOT_FOUND_POSTS("803", "게시글 목록을 찾을 수 없습니다."),
    DUPLICATE_REPORT("804", "이미 신고된 게시글입니다."),
    UNABLE_REPORT_MY_POST("805", "자신의 게시글은 신고할 수 없습니다."),
    UNAUTHORIZED_DELETE_POST("806", "해당 게시글 삭제 권한이 없습니다."),
    UNAUTHORIZED_UPDATE_POST("807", "해당 게시글 수정 권한이 없습니다.");


    private final String code;
    private final String message;

    PostErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
