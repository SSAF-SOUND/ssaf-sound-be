package com.ssafy.ssafsound.domain.member.dto;


import com.ssafy.ssafsound.domain.member.domain.AccountState;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
                .oauthType(OAuthType.valueOf(oauthName.toUpperCase()))
                .build();
    }
}
