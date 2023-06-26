package com.ssafy.ssafsound.domain.recruitapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetRecruitApplicationsResDto {

    private List<RecruitApplicationElement> recruitApplications;

    public GetRecruitApplicationsResDto(List<RecruitApplicationElement> recruitApplications) {
        this.recruitApplications = recruitApplications;

        recruitApplications.sort((r1, r2)->{
            if(r1.getIsLike().equals(r2.getIsLike())) {
                return r1.getRecruitApplicationId().compareTo(r2.getRecruitApplicationId());
            }
            return Boolean.compare(r2.getIsLike(), r1.getIsLike());
        });
    }
}
