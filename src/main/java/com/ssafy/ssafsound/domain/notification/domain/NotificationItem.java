package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.notification.converter.NotificationTypeConverter;
import com.ssafy.ssafsound.domain.notification.converter.ServiceTypeConverter;
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
}
