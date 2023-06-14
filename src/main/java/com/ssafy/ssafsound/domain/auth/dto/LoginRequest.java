package com.ssafy.ssafsound.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "코드가 유효하지 않습니다.")
    private String code;

    @NotBlank(message = "Auth 타입이 유효하지 않습니다.")
    private String oauthTypeName;
}
