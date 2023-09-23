package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberJoinRecruitsReqDto {
    private Long cursor;
    private Integer size;
    private Long memberId;
    private String category;
}
