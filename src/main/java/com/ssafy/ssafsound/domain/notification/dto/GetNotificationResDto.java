package com.ssafy.ssafsound.domain.notification.dto;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetNotificationResDto {
    private Long ownerId;
    private List<GetNotificationElement> notifications;

    public static GetNotificationResDto from(Notification notification) {
        return GetNotificationResDto.builder()
                .ownerId(notification.getOwnerId())
                .notifications(notification.getNotificationItems().stream()
                        .map(GetNotificationElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
