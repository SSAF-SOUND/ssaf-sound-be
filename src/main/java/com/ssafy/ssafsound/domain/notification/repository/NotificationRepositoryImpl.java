package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.domain.NotificationItem;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
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
    public void saveNotification(NotificationEvent notificationEvent) {
        Notification emptyNotification = Notification.from(notificationEvent);
        mongoTemplate.save(emptyNotification);
    }

    @Override
    public void saveNotificationItem(NotificationEvent notificationEvent) {
        NotificationItem notificationItem = NotificationItem.from(notificationEvent);

        Query query = new Query(Criteria.where("ownerId").is(notificationEvent.getOwnerId()));
        Update update = new Update().push("notificationItems", notificationItem);

        mongoTemplate.updateFirst(query, update, Notification.class);
    }

}
