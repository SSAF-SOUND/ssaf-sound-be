package com.ssafy.ssafsound.domain.notification.dto;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetNotificationResDto {
    private List<GetNotificationElement> notifications;
    private Long cursor;

    public static GetNotificationResDto of(List<Notification> notifications, Integer pageSize) {
        int size = notifications.size();
        boolean hasNext = size == pageSize + 1;

        return GetNotificationResDto.builder()
                .notifications((hasNext ? notifications.subList(0, size - 1) : notifications)
                        .stream()
                        .map(GetNotificationElement::from)
                        .collect(Collectors.toList()))
                .cursor(hasNext ? notifications.get(size - 1).getId() : null)
                .build();
    }
}
