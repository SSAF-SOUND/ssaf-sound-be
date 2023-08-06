package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRecruitLimitDetail extends RecruitLimitElement {
    private int currentNumber;

    @Override
    public void addRegisterLimit() {
        super.addRegisterLimit();
        currentNumber++;
    }

    public GetRecruitLimitDetail(String recruitType, Integer limit, Integer currentNumber) {
        super(recruitType, limit);
        this.currentNumber = currentNumber;
    }

    public static GetRecruitLimitDetail from(RecruitLimitation recruitLimitation) {
        return new GetRecruitLimitDetail(
                recruitLimitation.getType().getName(),
                recruitLimitation.getLimitation(),
                recruitLimitation.getCurrentNumber()
        );
    }
}
