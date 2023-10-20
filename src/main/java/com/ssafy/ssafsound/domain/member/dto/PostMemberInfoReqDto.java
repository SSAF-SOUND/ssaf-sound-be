package com.ssafy.ssafsound.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.ssafsound.domain.member.validator.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostMemberInfoReqDto {

    @NotNull
    @Size(min = 1, max = 11)
    private String nickname;

    @NotNull
    private Boolean ssafyMember;

    @NotNull
    private Boolean isMajor;

    @Semester
    private Integer semester;

    private String campus;

    @Builder.Default
    private Set<Long> termIds = new HashSet<>();

    @JsonIgnore
    public boolean isNotSemesterPresent() {
        return this.semester == null;
    }
}
