package com.ssafy.ssafsound.domain.notification.event;

import com.ssafy.ssafsound.domain.notification.domain.NotificationType;
import com.ssafy.ssafsound.domain.notification.domain.ServiceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationEvent {
    private Long ownerId;
    private String message;
    private Long contentId;
    private ServiceType serviceType;
    private NotificationType notificationType;
}
