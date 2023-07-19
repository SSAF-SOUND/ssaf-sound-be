package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantElement {
    private Long memberId;
    private String nickName;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;
}
