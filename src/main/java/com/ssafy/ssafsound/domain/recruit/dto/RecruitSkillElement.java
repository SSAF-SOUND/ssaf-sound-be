package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitSkill;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruitSkillElement {
    private Integer skillId;
    private String name;

    public static RecruitSkillElement from(RecruitSkill recruitSkill) {
        return RecruitSkillElement.builder()
                .skillId(recruitSkill.getSkill().getId())
                .name(recruitSkill.getSkill().getName())
                .build();
    }
}
