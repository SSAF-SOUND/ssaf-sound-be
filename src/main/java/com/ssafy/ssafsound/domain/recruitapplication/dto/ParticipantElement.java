package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipantElement {
    private Long recruitApplicationId;
    private LocalDateTime joinedAt;
    private Long memberId;
    private String nickname;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;
}
