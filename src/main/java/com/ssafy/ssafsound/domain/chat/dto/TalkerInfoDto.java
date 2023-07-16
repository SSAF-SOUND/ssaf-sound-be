package com.ssafy.ssafsound.domain.chat.dto;

import com.ssafy.ssafsound.domain.chat.domain.Talker;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TalkerInfoDto {

    private Long talkerId;

    private String nickname;

    private SSAFYInfo ssafyInfo;

    public static TalkerInfoDto from(Talker talker) {

        return TalkerInfoDto.builder()
                .talkerId(talker.getId())
                .nickname(talker.getMember().getNickname())
                .ssafyInfo(SSAFYInfo.from(talker.getMember()))
                .build();
    }
}
