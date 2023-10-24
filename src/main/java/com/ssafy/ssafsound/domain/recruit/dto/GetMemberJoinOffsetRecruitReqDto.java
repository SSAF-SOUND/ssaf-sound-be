package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class GetMemberJoinOffsetRecruitReqDto {
    private Integer page;
    private Integer size;
    private Long memberId;
    private String category;
}
