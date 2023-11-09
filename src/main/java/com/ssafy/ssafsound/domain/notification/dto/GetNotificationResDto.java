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

    public static GetNotificationResDto from(List<Notification> notifications) {
        return GetNotificationResDto.builder()
                .notifications(notifications.stream()
                        .map(GetNotificationElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
