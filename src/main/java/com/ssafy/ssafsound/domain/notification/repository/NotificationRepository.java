package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, NotificationCustomRepository {
    List<Notification> findAllByOwnerId(Long ownerId);

    Optional<Notification> findByOwnerId(Long ownerId);

    Boolean existsByOwnerId(Long ownerId);
}
