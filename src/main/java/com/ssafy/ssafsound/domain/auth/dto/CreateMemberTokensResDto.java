package com.ssafy.ssafsound.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class CreateMemberTokensResDto {
    @NotBlank(message = "코드가 유효하지 않습니다.")
    private String accessToken;

    @NotBlank(message = "Auth 타입이 유효하지 않습니다.")
    private String refreshToken;

    public static CreateMemberTokensResDto of(String accessToken, String refreshToken) {
        return CreateMemberTokensResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
