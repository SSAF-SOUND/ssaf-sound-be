package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.converter.NotificationTypeConverter;
import com.ssafy.ssafsound.domain.notification.converter.ServiceTypeConverter;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "notifications")
public class Notification {
    @Transient
    public static final String SEQUENCE_NAME = "NOTIFICATIONS_SEQUENCE";

    @Id
    private Long id;

    @Indexed
    private Long ownerId;

    private String message;

    private Long contentId;

    @Convert(converter = ServiceTypeConverter.class)
    private ServiceType serviceType;

    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType notificationType;

    @Builder.Default
    private Boolean read = Boolean.FALSE;

    @Indexed(name = "createdAtIndex", expireAfter = "30d")
    @CreatedDate
    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public static Notification from(NotificationEvent notificationEvent) {
        return Notification.builder()
                .ownerId(notificationEvent.getOwnerId())
                .message(notificationEvent.getMessage())
                .contentId(notificationEvent.getContentId())
                .serviceType(notificationEvent.getServiceType())
                .notificationType(notificationEvent.getNotificationType())
                .build();
    }
}
