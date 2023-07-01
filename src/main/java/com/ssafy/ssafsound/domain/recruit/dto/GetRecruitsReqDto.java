package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class GetRecruitsReqDto {
    @NotBlank
    private String category;
    private String keyword;
}
