package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;

import java.util.List;

public interface NotificationCustomRepository {

    List<Notification> findAllByOwnerId(Long ownerId, Long cursor, Integer size);
    void updateReadTrueByOwnerId(Long ownerId);

    Boolean existsNewNotification(Long ownerId);
}
