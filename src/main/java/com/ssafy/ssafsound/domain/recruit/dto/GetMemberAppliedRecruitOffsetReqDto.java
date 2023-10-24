package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberAppliedRecruitOffsetReqDto {
    private String category;
    private String matchStatus;
    private Integer page;
    private Integer size;
}
