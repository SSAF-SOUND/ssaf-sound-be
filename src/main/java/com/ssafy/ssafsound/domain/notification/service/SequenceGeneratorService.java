package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.notification.domain.NotificationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    public Long generateSequence(String sequenceName) {
        NotificationSequence notificationSequence = mongoOperations.findAndModify(
                new Query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                NotificationSequence.class);

        return !Objects.isNull(notificationSequence) ? notificationSequence.getSeq() : 1;
    }
}
