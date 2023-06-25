package com.ssafy.ssafsound.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostNicknameReqDto {

    @NotBlank
    private String nickname;
}