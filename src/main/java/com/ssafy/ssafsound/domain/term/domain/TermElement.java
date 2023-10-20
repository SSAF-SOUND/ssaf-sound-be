package com.ssafy.ssafsound.domain.term.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermElement {

    private Long termId;

    private String termName;

    private String content;

    private Boolean required;

    private Integer sequence;

    public static TermElement from(Term term) {
        return TermElement.builder()
                .termId(term.getId())
                .termName(term.getName())
                .content(term.getContent())
                .required(term.getRequired())
                .sequence(term.getSequence())
                .build();
    }
}
