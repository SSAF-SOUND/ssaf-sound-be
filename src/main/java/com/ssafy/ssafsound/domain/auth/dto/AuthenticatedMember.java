package com.ssafy.ssafsound.domain.auth.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedMember {
    private Long memberId;

    private String memberRole;

    public static AuthenticatedMember of(Member member) {
        return AuthenticatedMember.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .build();
    }
}
