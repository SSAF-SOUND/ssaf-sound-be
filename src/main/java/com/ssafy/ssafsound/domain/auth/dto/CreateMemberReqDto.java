package com.ssafy.ssafsound.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class CreateMemberReqDto {
    @NotBlank(message = "코드가 유효하지 않습니다.")
    private String code;

    @Pattern(regexp = "^(google|kakao|github|apple)$")
    @NotBlank(message = "Oauth name이 유효하지 않습니다.")
    private String oauthName;
}
