package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.exception.NotificationErrorInfo;
import com.ssafy.ssafsound.domain.notification.exception.NotificationException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ServiceType {
    SYSTEM(1, "SYSTEM"),
    POST(2, "POST"),
    RECRUIT(3, "RECRUIT");

    private final Integer id;
    private final String name;

    ServiceType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ServiceType ofName(String name) {
        return Arrays.stream(ServiceType.values())
                .filter(value -> value.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new NotificationException(NotificationErrorInfo.NOT_FOUND_SERVICE_TYPE));
    }
}
