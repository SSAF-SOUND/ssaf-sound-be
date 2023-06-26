package com.ssafy.ssafsound.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostNicknameResDto {
    boolean possible;

    public static PostNicknameResDto of(boolean possible) {
        return PostNicknameResDto.builder()
                .possible(possible)
                .build();
    }
}