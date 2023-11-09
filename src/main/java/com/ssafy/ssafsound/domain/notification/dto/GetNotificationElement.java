package com.ssafy.ssafsound.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.ssafsound.domain.notification.domain.NotificationItem;
import com.ssafy.ssafsound.domain.notification.domain.NotificationType;
import com.ssafy.ssafsound.domain.notification.domain.ServiceType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class GetNotificationElement {
    private String message;
    private Long contentId;
    private ServiceType serviceType;
    private NotificationType notificationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    public static GetNotificationElement from(NotificationItem notificationItem) {
        return GetNotificationElement.builder()
                .message(notificationItem.getMessage())
                .contentId(notificationItem.getContentId())
                .serviceType(notificationItem.getServiceType())
                .notificationType(notificationItem.getNotificationType())
                .createAt(notificationItem.getCreatedAt())
                .build();
    }
}
