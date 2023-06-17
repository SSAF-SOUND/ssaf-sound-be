package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLunchPollResDto {

    private Long pollCount;

    public static PostLunchPollResDto of(Long pollCount){
        return PostLunchPollResDto.builder()
                .pollCount(pollCount)
                .build();
    }
}
