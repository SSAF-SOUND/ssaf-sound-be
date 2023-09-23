package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppliedRecruitElement extends RecruitElement {

    private MatchStatus matchStatus;
    private LocalDateTime appliedAt;

    public static AppliedRecruitElement fromRecruitAndLoginMemberId(AppliedRecruit appliedRecruit, Long memberId) {
        AppliedRecruitElement appliedRecruitElement = new AppliedRecruitElement(RecruitElement.fromRecruitAndLoginMemberId(appliedRecruit.getRecruit(), memberId));
        appliedRecruitElement.setMatchStatus(appliedRecruit.getMatchStatus());
        appliedRecruitElement.setAppliedAt(appliedRecruit.getAppliedAt());
        return appliedRecruitElement;
    }

    private AppliedRecruitElement(RecruitElement recruitElement) {
        super(recruitElement.getRecruitId(),
                recruitElement.getCategory(),
                recruitElement.getTitle(),
                recruitElement.isFinishedRecruit(),
                recruitElement.getRecruitEnd(),
                recruitElement.getContent(),
                recruitElement.getSkills(),
                recruitElement.getParticipants(),
                recruitElement.getMine());
    }

    private void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    private void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

}
