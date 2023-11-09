package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;

import java.util.List;

public interface NotificationCustomRepository {

    List<Notification> findAllByOwnerAndReadTrue(Long owner);
}
