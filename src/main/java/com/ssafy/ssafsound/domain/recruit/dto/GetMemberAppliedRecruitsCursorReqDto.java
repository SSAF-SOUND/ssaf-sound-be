package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberAppliedRecruitsCursorReqDto {
    private String category;
    private String matchStatus;
    private Long cursor;
    private Integer size;
}
