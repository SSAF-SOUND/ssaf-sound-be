package com.ssafy.ssafsound.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMemberTokensResDto {

    private String accessToken;

    private String refreshToken;

    public static CreateMemberTokensResDto of(String accessToken, String refreshToken) {
        return CreateMemberTokensResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
