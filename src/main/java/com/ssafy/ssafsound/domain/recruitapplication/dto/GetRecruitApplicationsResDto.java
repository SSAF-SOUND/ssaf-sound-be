package com.ssafy.ssafsound.domain.recruitapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class GetRecruitApplicationsResDto {

    private Long recruitId;
    private Map<String, List<RecruitApplicationElement>> recruitApplications;

    public GetRecruitApplicationsResDto(Long recruitId, List<RecruitApplicationElement> recruitApplications) {
        this.recruitId = recruitId;
        this.recruitApplications = new HashMap<>();
        recruitApplications.sort((r1, r2)->{
            if(r1.getLiked().equals(r2.getLiked())) {
                return r1.getRecruitApplicationId().compareTo(r2.getRecruitApplicationId());
            }
            return Boolean.compare(r2.getLiked(), r1.getLiked());
        });

        recruitApplications.forEach(recruitApplicationElement -> {
            List<RecruitApplicationElement> applications = this.recruitApplications
                    .getOrDefault(recruitApplicationElement.getRecruitType(), new ArrayList<>());
            applications.add(recruitApplicationElement);
            this.recruitApplications.put(recruitApplicationElement.getRecruitType(), applications);
        });
    }
}
