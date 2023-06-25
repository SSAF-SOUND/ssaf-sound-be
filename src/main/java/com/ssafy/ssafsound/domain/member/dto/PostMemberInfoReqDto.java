package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.validator.Semester;
import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
public class PostMemberInfoReqDto {

    @NotNull
    @Size(min = 1, max = 11)
    private String nickname;

    @NotNull
    private Boolean ssafyMember;

    @Semester
    private Integer semester;

    private Boolean isMajor;

    private String campus;
}
