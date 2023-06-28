package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.Getter;

@Getter
public class RecruitApplicationElement {
    @JsonProperty
    private Long recruitApplicationId;

    @JsonProperty
    private String recruitType;

    @JsonProperty
    private MatchStatus matchStatus;

    @JsonProperty
    private MetaData type;

    @JsonProperty
    private Long memberId;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private SSAFYInfo ssafyInfo;

    @JsonProperty
    private String reply;

    @JsonProperty
    private String question;

    @JsonProperty
    private Boolean isLike;

    public RecruitApplicationElement(Long recruitApplicationId, MetaData recruitType, MatchStatus matchStatus, MetaData type,
                                     Long memberId, String nickName,
                                     Integer semester, Boolean major, MetaData campus,
                                     AuthenticationStatus certificationState, String majorType,
                                     String reply, String question, Boolean isLike) {

        this.recruitApplicationId = recruitApplicationId;
        this.recruitType = recruitType.getName();
        this.matchStatus = matchStatus;
        this.type = type;
        this.memberId = memberId;
        this.nickName = nickName;
        this.ssafyInfo = SSAFYInfo.builder()
                .semester(semester)
                .isMajor(major)
                .campus(campus == null ? null : campus.getName())
                .certificationState(certificationState == null ? null : certificationState.name())
                .majorType(majorType)
                .build();
        this.reply = reply;
        this.question = question;
        this.isLike = isLike;
    }
}