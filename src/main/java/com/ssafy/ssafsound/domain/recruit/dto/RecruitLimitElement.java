package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitLimitElement {
    private String recruitType;
    private int limit;

    public static RecruitLimitElement from(RecruitLimitation recruitLimitation) {
        return new RecruitLimitElement(recruitLimitation.getType().getName(), recruitLimitation.getLimitation());
    }
}
