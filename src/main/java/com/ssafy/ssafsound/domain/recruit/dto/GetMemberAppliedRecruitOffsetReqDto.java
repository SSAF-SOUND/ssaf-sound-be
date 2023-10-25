package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberAppliedRecruitOffsetReqDto extends RecruitOffsetPagingRequestDto {
    private String category;
    private String matchStatus;
}
