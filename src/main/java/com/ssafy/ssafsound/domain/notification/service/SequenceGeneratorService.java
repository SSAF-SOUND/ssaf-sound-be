package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.notification.domain.AutoIncrementSequence;
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
        AutoIncrementSequence autoIncrementSequence = mongoOperations.findAndModify(
                new Query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                AutoIncrementSequence.class);

        return !Objects.isNull(autoIncrementSequence) ? autoIncrementSequence.getSequence() : 1;
    }
}
