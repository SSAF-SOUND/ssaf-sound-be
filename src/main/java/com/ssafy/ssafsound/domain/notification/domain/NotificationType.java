package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.exception.NotificationErrorInfo;
import com.ssafy.ssafsound.domain.notification.exception.NotificationException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NotificationType {
    POST(1, "POST"),
    RECRUIT(2, "RECRUIT"),
    SYSTEM(3, "SYSTEM");

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
