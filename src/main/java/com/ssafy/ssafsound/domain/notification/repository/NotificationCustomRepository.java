package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;

public interface NotificationCustomRepository {

    void updateReadTrue(Long owner);

    void saveNotification(NotificationEvent notificationEvent);

    void saveNotificationItem(NotificationEvent notificationEvent);
}
