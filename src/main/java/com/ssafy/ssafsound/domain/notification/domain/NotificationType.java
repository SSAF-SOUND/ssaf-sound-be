package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.exception.NotificationErrorInfo;
import com.ssafy.ssafsound.domain.notification.exception.NotificationException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NotificationType {
    POST_REPLAY(1, "POST_REPLAY"),
    COMMENT_REPLAY(2, "COMMENT_REPLAY"),
    RECRUIT(3, "RECRUIT"),
    SYSTEM(4, "SYSTEM");

    private final Integer id;
    private final String name;

    NotificationType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static NotificationType ofName(String name) {
        return Arrays.stream(NotificationType.values())
                .filter(value -> value.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new NotificationException(NotificationErrorInfo.NOT_FOUND_NOTIFICATION_TYPE));
    }
}
