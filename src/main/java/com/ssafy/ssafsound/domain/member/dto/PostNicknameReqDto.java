package com.ssafy.ssafsound.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostNicknameReqDto {

    @NotNull
    @Size(min = 1, max = 11)
    private String nickname;
}