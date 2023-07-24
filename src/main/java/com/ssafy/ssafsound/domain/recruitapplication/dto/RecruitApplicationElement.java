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
    private String nickname;

    @JsonProperty
    private Boolean isMajor;

    @JsonProperty
    private SSAFYInfo ssafyInfo;

    @JsonProperty
    private String reply;

    @JsonProperty
    private String question;

    @JsonProperty
    private Boolean isLike;

    public RecruitApplicationElement(Long recruitApplicationId, MetaData recruitType, MatchStatus matchStatus, MetaData type,
                                     Long memberId, String nickname,
                                     Integer semester, Boolean major, MetaData campus,
                                     AuthenticationStatus certificationState, MetaData majorTrack,
                                     String reply, String question, Boolean isLike) {

        this.recruitApplicationId = recruitApplicationId;
        this.recruitType = recruitType.getName();
        this.matchStatus = matchStatus;
        this.type = type;
        this.memberId = memberId;
        this.nickname = nickname;
        this.isMajor = major;
        this.ssafyInfo = SSAFYInfo.of(semester, campus.getName(), certificationState.name(), majorTrack);
        this.reply = reply;
        this.question = question;
        this.isLike = isLike;
    }
}
