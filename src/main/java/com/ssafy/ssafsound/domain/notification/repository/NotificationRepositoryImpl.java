package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.domain.NotificationItem;
import com.ssafy.ssafsound.domain.notification.dto.CreateNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryImpl implements NotificationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateReadTrue(Long owner) {
        Query query = new Query(Criteria.where("owner").is(owner));
        Update update = new Update().set("notificationItems.$[].read", true);

        mongoTemplate.updateMulti(query, update, Notification.class);
    }

    @Override
    public void saveNotification(CreateNotification createNotification) {
        Notification emptyNotification = Notification.from(createNotification);
        mongoTemplate.save(emptyNotification);
    }

    @Override
    public void saveNotificationItem(CreateNotification createNotification) {
        NotificationItem notificationItem = NotificationItem.from(createNotification);

        Query query = new Query(Criteria.where("ownerId").is(createNotification.getOwnerId()));
        Update update = new Update().push("notificationItems", notificationItem);

        mongoTemplate.updateFirst(query, update, Notification.class);
    }

}
