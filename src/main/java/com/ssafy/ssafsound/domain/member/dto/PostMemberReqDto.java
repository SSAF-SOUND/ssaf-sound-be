package com.ssafy.ssafsound.domain.member.dto;


import com.ssafy.ssafsound.domain.member.domain.AccountState;
import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
public class PostMemberReqDto {

    @NotBlank
    private String oauthName;

    @NotBlank
    private String oauthIdentifier;

    public Member createMember() {
        return Member.builder()
                .oauthIdentifier(oauthIdentifier)
                .accountState(AccountState.NORMAL)
                .build();
    }
}
