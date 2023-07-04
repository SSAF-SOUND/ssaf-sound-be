package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.validator.Semester;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
public class PostCertificationInfoReqDto {

    @Semester
    private Integer semester;

    @NotBlank
    private String answer;
}
