package com.ssafy.ssafsound.domain.notification.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.notification.converter.NotificationTypeConverter;
import com.ssafy.ssafsound.domain.notification.converter.ServiceTypeConverter;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Convert;

@Getter
@Builder
public class NotificationElement extends BaseTimeEntity {
    private Long notificationId;

    private String message;

    @Convert(converter = ServiceTypeConverter.class)
    private ServiceType serviceType;

    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType notificationType;

    private Long contentId;

    @Builder.Default
    private Boolean read = Boolean.FALSE;
}
