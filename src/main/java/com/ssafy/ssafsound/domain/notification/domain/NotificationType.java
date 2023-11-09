package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.exception.NotificationErrorInfo;
import com.ssafy.ssafsound.domain.notification.exception.NotificationException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NotificationType {
    SYSTEM(1, "SYSTEM"),
    POST_REPLAY(2, "POST_REPLAY"),
    COMMENT_REPLAY(3, "COMMENT_REPLAY"),
    RECRUIT(4, "RECRUIT");

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
