package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMemberDefaultInfoResDto {

    private String nickname;

    private Boolean ssafyMember = false;

    private Boolean isMajor;

    private SSAFYInfo ssafyInfo;

    private GetMemberDefaultInfoResDto(String nickname, Boolean isMajor, Member member) {
        this(nickname, isMajor);
        this.ssafyMember = true;
        this.ssafyInfo = SSAFYInfo.from(member);
    }

    private GetMemberDefaultInfoResDto(String nickname, Boolean isMajor) {
        this.nickname = nickname;
        this.isMajor = isMajor;
    }

    public static GetMemberDefaultInfoResDto from(Member member) {
        if (member.getSsafyMember()) {
            return new GetMemberDefaultInfoResDto(member.getNickname(), member.getMajor(), member);
        }
        return new GetMemberDefaultInfoResDto(member.getNickname(), member.getMajor());
    }
}