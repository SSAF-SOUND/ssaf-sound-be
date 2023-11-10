package com.ssafy.ssafsound.domain.notification.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "AutoIncrementSequences")
public class AutoIncrementSequence {
    @Id
    private String id;

    private Long sequence;
}
