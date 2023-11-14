package com.ssafy.ssafsound.domain.notification.exception;

import lombok.Getter;

@Getter
public enum NotificationErrorInfo {
    NOT_FOUND_SERVICE_TYPE("1200", "잘못된 서비스 타입입니다."),
    NOT_FOUND_NOTIFICATION_TYPE("1201", "잘못된 알림 타입입니다."),
    NOT_FOUND_NOTIFICATION_BY_OWNER("1202", "해당 사용자의 알림이 없습니다.");

    private final String code;
    private final String message;

    NotificationErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
