package com.ssafy.ssafsound.domain.notification.repository;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryImpl implements NotificationCustomRepository {
    private final MongoOperations mongoOperations;

    @Override
    public List<Notification> findAllByOwnerAndReadTrue(Long ownerId, Long cursor, Integer size) {
        Criteria criteria = Criteria.where("ownerId").is(ownerId);
        if (cursor != -1) {
            criteria.and("_id").lt(cursor);
        }
        Query query = new Query(criteria)
                .limit(size + 1)
                .with(Sort.by(Sort.Order.desc("_id")));

        List<Notification> notifications = mongoOperations.find(query, Notification.class);

        mongoOperations.updateMulti(
                new Query(Criteria.where("ownerId").is(ownerId)),
                new Update().set("read", true),
                Notification.class
        );

        return notifications;
    }

    @Override
    public Boolean existsNewNotification(Long ownerId) {
        Query query = new Query(Criteria.where("ownerId").is(ownerId).and("read").is(false));
        return mongoOperations.exists(query, Notification.class);
    }


}
