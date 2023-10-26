package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Participant {
    private String nickname;
    private Boolean isMajor;
}
