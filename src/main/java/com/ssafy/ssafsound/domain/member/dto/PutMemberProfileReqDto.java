package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.meta.validator.CheckSkills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PutMemberProfileReqDto {

    @NotBlank
    private String introduceMyself;

    @CheckSkills
    private List<String> skills;

    private List<PutMemberLink> memberLinks;
}
