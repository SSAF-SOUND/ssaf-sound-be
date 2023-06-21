package com.ssafy.ssafsound.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMemberAccessTokenResDto {
    private String accessToken;

    public static CreateMemberAccessTokenResDto of(String accessToken) {
        return CreateMemberAccessTokenResDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
