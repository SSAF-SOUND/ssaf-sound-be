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
    private SSAFYInfo ssafyInfo;

    public static GetMemberResDto fromGeneralUser(Member member, MemberRole memberRole) {
        return GetMemberResDto.builder()
                .memberId(member.getId())
                .memberRole(memberRole.getRoleType())
                .nickname(member.getNickname())
                .ssafyMember(member.getSsafyMember())
                .build();
    }

    public static GetMemberResDto fromSSAFYUser(Member member, MemberRole memberRole) {
        return GetMemberResDto.builder()
                .memberId(member.getId())
                .memberRole(memberRole.getRoleType())
                .nickname(member.getNickname())
                .ssafyMember(member.getSsafyMember())
                .ssafyInfo(SSAFYInfo.builder()
                        .semester(member.getSemester())
                        .isMajor(member.getMajor())
                        .campus(member.getCampus().getName())
                        .certificationState(member.getCertificationState().name())
                        .build())
                .build();
    }
}
