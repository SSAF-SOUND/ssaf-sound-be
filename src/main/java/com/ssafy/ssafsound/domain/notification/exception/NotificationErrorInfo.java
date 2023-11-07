package com.ssafy.ssafsound.domain.notification.exception;

import lombok.Getter;

@Getter
public enum NotificationErrorInfo {
    NOT_FOUND_SERVICE_TYPE("xxx", "잘못된 서비스 타입입니다."),
    NOT_FOUND_NOTIFICATION_TYPE("xxx", "잘못된 알림 타입입니다.");

    private final String code;
    private final String message;

    NotificationErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
