package com.ssafy.ssafsound.domain.auth.dto;

import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
public class CreateMemberReqDto {
    @NotBlank(message = "코드가 유효하지 않습니다.")
    private String code;

    @NotBlank
    private String oauthName;
}
