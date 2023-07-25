package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMemberResDto {

    private Long memberId;
    private String memberRole;
    private String nickname;
    private Boolean ssafyMember;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public static GetMemberResDto fromGeneralUser(Member member) {
        return GetMemberResDto.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .nickname(member.getNickname())
                .ssafyMember(member.getSsafyMember())
                .isMajor(member.getMajor())
                .build();
    }

    public static GetMemberResDto fromSSAFYUser(Member member) {
        return GetMemberResDto.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .nickname(member.getNickname())
                .ssafyMember(member.getSsafyMember())
                .isMajor(member.getMajor())
                .ssafyInfo(SSAFYInfo.from(member))
                .build();
    }
}
