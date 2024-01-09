package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, NotificationCustomRepository {
    Page<Notification> findAllByOwnerId(Long ownerId, Pageable pageable);
}
