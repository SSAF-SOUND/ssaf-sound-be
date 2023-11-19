package com.ssafy.ssafsound.domain.notification.message;

import lombok.Getter;

@Getter
public enum NotificationMessage {
    POST_REPLY_MESSAGE("'%s' 게시글에 새로운 댓글이 달렸습니다."),
    COMMENT_REPLY_MESSAGE("'%s' 게시글에 새로운 대댓글이 달렸습니다.");

    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
