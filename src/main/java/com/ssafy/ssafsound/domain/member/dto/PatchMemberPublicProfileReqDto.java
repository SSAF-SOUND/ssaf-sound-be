package com.ssafy.ssafsound.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatchMemberPublicProfileReqDto {

    private Boolean isPublic;
}
