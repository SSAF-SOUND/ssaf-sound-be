package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
