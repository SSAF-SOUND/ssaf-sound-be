package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.converter.NotificationTypeConverter;
import com.ssafy.ssafsound.domain.notification.converter.ServiceTypeConverter;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
import lombok.*;

import javax.persistence.Convert;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationItem {
    private String message;

    private Long contentId;

    @Convert(converter = ServiceTypeConverter.class)
    private ServiceType serviceType;

    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType notificationType;

    @Builder.Default
    private Boolean read = Boolean.FALSE;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static NotificationItem from(NotificationEvent notificationEvent) {
        return NotificationItem.builder()
                .message(notificationEvent.getMessage())
                .contentId(notificationEvent.getContentId())
                .serviceType(notificationEvent.getServiceType())
                .notificationType(notificationEvent.getNotificationType())
                .build();
    }
}
