package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class GetMemberJoinOffsetRecruitReqDto extends RecruitOffsetPagingRequestDto {
    private Long memberId;
    private String category;
}
