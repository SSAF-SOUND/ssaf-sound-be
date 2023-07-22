package com.ssafy.ssafsound.domain.chat.dto;

import com.ssafy.ssafsound.domain.chat.domain.Talker;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TalkerInfoDto {

    private String nickname;

    private Boolean isMajor;

    private Boolean ssafyMember;

    private SSAFYInfo ssafyInfo;

    public static TalkerInfoDto from(Talker talker) {

        return TalkerInfoDto.builder()
                .nickname(talker.getMember().getNickname())
                .isMajor(talker.getMember().getMajor())
                .ssafyMember(talker.getMember().getSsafyMember())
                .ssafyInfo(talker.getMember().getSsafyMember() ? SSAFYInfo.from(talker.getMember()) : null)
                .build();
    }

}
