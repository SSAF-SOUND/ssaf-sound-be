package com.ssafy.ssafsound.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAccessTokenResDto {
    private String accessToken;

    public static CreateAccessTokenResDto of(String accessToken) {
        return CreateAccessTokenResDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
