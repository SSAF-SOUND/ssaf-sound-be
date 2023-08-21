package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.ssafsound.domain.recruit.validator.CheckRecruitLimitElement;
import com.ssafy.ssafsound.domain.meta.validator.CheckSkills;
import com.ssafy.ssafsound.domain.recruit.validator.CheckRecruitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchRecruitReqDto {

    @NotEmpty
    private String category;

    @CheckRecruitType
    private String registerRecruitType;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate recruitEnd;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private String contactURI;

    @CheckSkills
    private List<String> skills;

    @CheckRecruitLimitElement
    @Builder.Default
    private List<RecruitLimitElement> limitations = new ArrayList<>();
}
