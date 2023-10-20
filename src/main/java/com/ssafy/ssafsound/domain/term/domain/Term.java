package com.ssafy.ssafsound.domain.term.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name="term")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Term extends BaseTimeEntity {

    @Id
    @Column(name = "term_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private String name;

    @Column
    @Builder.Default
    private Boolean used = false;

    @Column
    @Builder.Default
    private Boolean required = false;

    @Column
    private Integer sequence;
}
