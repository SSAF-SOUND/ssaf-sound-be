package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.validator.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostCertificationInfoReqDto {

    @NotBlank
    private String majorTrack;

    @Semester
    private Integer semester;

    @NotBlank
    private String answer;
}
