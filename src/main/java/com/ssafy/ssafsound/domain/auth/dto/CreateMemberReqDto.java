package com.ssafy.ssafsound.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
@Builder
@Getter
public class CreateMemberReqDto {
    @NotBlank
    private String code;

    @NotBlank
    private String oauthName;
}
