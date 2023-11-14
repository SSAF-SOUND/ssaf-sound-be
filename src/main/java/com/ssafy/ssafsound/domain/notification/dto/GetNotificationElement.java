package com.ssafy.ssafsound.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.domain.NotificationType;
import com.ssafy.ssafsound.domain.notification.domain.ServiceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetNotificationElement {
    private Long notificationId;
    private String message;
    private Long contentId;
    private ServiceType serviceType;
    private NotificationType notificationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    public static GetNotificationElement from(Notification notification) {
        return GetNotificationElement.builder()
                .notificationId(notification.getId())
                .message(notification.getMessage())
                .contentId(notification.getContentId())
                .serviceType(notification.getServiceType())
                .notificationType(notification.getNotificationType())
                .createAt(notification.getCreatedAt())
                .build();
    }
}
