package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.converter.RecruitTypeConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="recruit")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_recruit = false")
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
    @Builder.Default
    private Boolean deletedRecruit = false;

    @Column
    @Builder.Default
    private Boolean finishedRecruit = false;

    @Convert(converter = RecruitTypeConverter.class)
    private MetaData registerRecruitType;

    @Column
    private String contactURI;

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

    public void setRegisterRecruitType(MetaData registerRecruitType) {
        this.registerRecruitType = registerRecruitType;
    }

    public void setRecruitQuestions(List<RecruitQuestion> questions) {
        this.questions = questions;
    }

    public void setRecruitSkill(List<RecruitSkill> skills) {
        this.skills = skills;
    }

    public void setRecruitLimitations(List<RecruitLimitation> limitations) {
        this.limitations = limitations;
    }

    public void update(PatchRecruitReqDto patchRecruitReqDto) {
        this.endDateTime = patchRecruitReqDto.getRecruitEnd().atTime(LocalTime.MAX);
        this.title = patchRecruitReqDto.getTitle();
        this.content = patchRecruitReqDto.getContent();
        this.contactURI = patchRecruitReqDto.getContactURI();
    }

    public void increaseView() {
        this.view++;
    }

    public void delete() {
        this.deletedRecruit = true;
    }

    public void expired() {
        this.finishedRecruit = true;
    }
}
