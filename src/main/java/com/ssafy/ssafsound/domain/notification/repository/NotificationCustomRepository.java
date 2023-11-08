package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.dto.CreateNotification;

public interface NotificationCustomRepository {

    void updateReadTrue(Long owner);

    void saveNotification(CreateNotification createNotification);

    void saveNotificationItem(CreateNotification createNotification);
}
