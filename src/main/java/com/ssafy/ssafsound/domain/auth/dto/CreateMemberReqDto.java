package com.ssafy.ssafsound.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateMemberReqDto {
    @NotBlank
    private String code;

    @NotBlank
    private String oauthName;
}
