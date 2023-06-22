package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetGeneralMemberResDto {

    private Long memberId;
    private String memberRole;
    private String nickname;
    private Boolean ssafyMember;

    public static GetGeneralMemberResDto from(Member member, MemberRole memberRole) {
        return new GetGeneralMemberResDto(member.getId(), memberRole.getRoleType(), member.getNickname(), member.getSsafyMember());
    }
}
