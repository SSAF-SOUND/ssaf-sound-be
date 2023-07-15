package com.ssafy.ssafsound.domain.chat.dto;

import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TalkerInfoDto {

    private Long talkerId;

    private String nickname;

    private SSAFYInfo ssafyInfo;
}
