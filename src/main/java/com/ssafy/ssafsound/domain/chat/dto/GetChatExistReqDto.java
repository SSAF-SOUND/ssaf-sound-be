package com.ssafy.ssafsound.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GetChatExistReqDto {

    @NotBlank
    private String sourceType;

    @NotNull
    private Long sourceId;
}
