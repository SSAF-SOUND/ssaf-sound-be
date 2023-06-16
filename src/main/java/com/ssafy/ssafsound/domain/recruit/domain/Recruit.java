package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="recruit")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruit extends BaseTimeEntity {

    @Id
    @Column(name = "recruit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Long view;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "recruit_start")
    private LocalDateTime startDateTime;

    @Column(name = "recruit_end")
    private LocalDateTime endDateTime;

    @Column
    private Boolean deletedRecruit;

    @Column
    private Boolean finishedRecruit;

    @OneToMany(mappedBy = "recruit", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Builder.Default
    private List<RecruitSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "recruit", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Builder.Default
    private List<RecruitQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "recruit", cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<RecruitLimitation> limitations = new ArrayList<>();

    @OneToMany(mappedBy = "recruit", cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<RecruitApplication> applications = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setRegister(Member member) {
        if(this.member != null) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        this.member = member;
    }

    public void setRecruitQuestions(List<RecruitQuestion> questions) {
        this.questions = questions;
    }

    public void setRecruitSkill(List<RecruitSkill> skills) {
        this.skills = skills;
    }
}
