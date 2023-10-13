package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class GetRecruitApplicationsResDto {
    private String category;
    private Long recruitId;
    private Map<String, List<RecruitApplicationResElement>> recruitApplications;

    public GetRecruitApplicationsResDto(Recruit recruit, List<RecruitApplicationElement> recruitApplications) {
        this.category = recruit.getCategory().name();
        this.recruitId = recruit.getId();
        this.recruitApplications = new HashMap<>();

        recruit.getLimitations().forEach(recruitLimitation ->
                this.recruitApplications.put(recruitLimitation.getType().getName(), new ArrayList<>()));

        recruitApplications.sort((r1, r2)->{
            if(r1.getLiked().equals(r2.getLiked())) {
                return r1.getRecruitApplicationId().compareTo(r2.getRecruitApplicationId());
            }
            return Boolean.compare(r2.getLiked(), r1.getLiked());
        });

        recruitApplications.forEach(recruitApplicationElement -> {
            List<RecruitApplicationResElement> applications = this.recruitApplications
                    .getOrDefault(recruitApplicationElement.getRecruitType(), new ArrayList<>());
            applications.add(new RecruitApplicationResElement(recruitApplicationElement));
            this.recruitApplications.put(recruitApplicationElement.getRecruitType(), applications);
        });
    }
}
