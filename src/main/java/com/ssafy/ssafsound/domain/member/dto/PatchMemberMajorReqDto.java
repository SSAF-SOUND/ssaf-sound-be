package com.ssafy.ssafsound.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatchMemberMajorReqDto {

    @NotNull
    private Boolean isMajor;
}
