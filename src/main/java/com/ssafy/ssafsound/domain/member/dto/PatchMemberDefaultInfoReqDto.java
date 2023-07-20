package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.validator.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatchMemberDefaultInfoReqDto {

    @NotNull
    private Boolean ssafyMember;

    @Semester
    private Integer semester;

    private String campus;

    public boolean isNotSemesterPresent() {
        return semester == null;
    }
}
