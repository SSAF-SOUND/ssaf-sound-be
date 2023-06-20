package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_question_reply")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitQuestionReply {

    @Id
    @Column(name = "recruit_question_reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_application_id")
    private RecruitApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_question_id")
    private RecruitQuestion question;
}
