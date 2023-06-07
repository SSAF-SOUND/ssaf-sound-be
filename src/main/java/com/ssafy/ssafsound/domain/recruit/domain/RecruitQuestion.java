package com.ssafy.ssafsound.domain.recruit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_question")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitQuestion {

    @Id
    @Column(name = "recruit_question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;
}
