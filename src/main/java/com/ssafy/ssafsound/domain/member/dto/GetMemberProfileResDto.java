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
public class GetMemberProfileResDto {

    private String nickname;

    private Boolean ssafyMember;

    private Boolean isMajor;

    private SSAFYInfo ssafyInfo;

    private GetMemberProfileResDto(String nickname, Boolean isMajor, Member member) {
        this.nickname = nickname;
        this.ssafyMember = true;
        this.isMajor = isMajor;
        this.ssafyInfo = SSAFYInfo.from(member);
    }

    private GetMemberProfileResDto(String nickname,  Boolean isMajor) {
        this.nickname = nickname;
        this.ssafyMember = false;
        this.isMajor = isMajor;
    }

    public static GetMemberProfileResDto from(Member member) {
        if (member.getSsafyMember()) {
            return new GetMemberProfileResDto(member.getNickname(), member.getMajor(), member);
        }
        return new GetMemberProfileResDto(member.getNickname(), member.getMajor());
    }
}