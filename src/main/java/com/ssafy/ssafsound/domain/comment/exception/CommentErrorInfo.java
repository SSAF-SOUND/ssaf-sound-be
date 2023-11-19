package com.ssafy.ssafsound.domain.comment.exception;

import lombok.Getter;

@Getter
public enum CommentErrorInfo {
    NOT_FOUND_COMMENT_NUMBER("806", "익명 번호를 찾을 수 없습니다."),
    NOT_FOUND_COMMENT("807", "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_UPDATE_COMMENT("808", "댓글을 수정할 권한이 없습니다"),
    UNAUTHORIZED_DELETE_COMMENT("809", "댓글을 삭제할 권한이 없습니다"),
    NOT_ASSOCIATED_WITH_POST("810", "댓글과 게시글이 일치하지 않습니다."),
    FORBIDDEN_REPLY_SUB_COMMENT("811", "대댓글에는 답변을 작성할 수 없습니다.");

    private final String code;
    private final String message;

    CommentErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
