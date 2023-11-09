package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryImpl implements NotificationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Notification> findAllByOwnerAndReadTrue(Long ownerId) {
        Query query = new Query(Criteria.where("ownerId").is(ownerId))
                .with(Sort.by(Sort.Order.desc("createdAt")));
        Update update = new Update().set("read", true);

        List<Notification> notifications = mongoTemplate.find(query, Notification.class);
        mongoTemplate.updateMulti(query, update, Notification.class);
        return notifications;
    }
}
