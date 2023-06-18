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
public class AuthenticatedUser {
    private Long memberId;

    private String memberRole;

    public static AuthenticatedUser of(Member member) {
        return AuthenticatedUser.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .build();
    }
}
