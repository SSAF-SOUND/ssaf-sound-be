package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLunchPollResDto {

    private Integer pollCount;

    public static PostLunchPollResDto of(Integer pollCount){
        return PostLunchPollResDto.builder()
                .pollCount(pollCount)
                .build();
    }
}
