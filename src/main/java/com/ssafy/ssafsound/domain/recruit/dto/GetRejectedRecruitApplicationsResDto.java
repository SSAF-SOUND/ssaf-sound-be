package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationResElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetRejectedRecruitApplicationsResDto {
    private String category;
    private Long recruitId;
    private List<RecruitApplicationResElement> recruitApplications;

    public GetRejectedRecruitApplicationsResDto(Recruit recruit, List<RecruitApplicationElement> recruitApplications) {
        this.category = recruit.getCategory().name();
        this.recruitId = recruit.getId();
        this.recruitApplications = new LinkedList<>();

        recruitApplications.sort((r1, r2)->{
            if(r1.getLiked().equals(r2.getLiked())) {
                return r1.getRecruitApplicationId().compareTo(r2.getRecruitApplicationId());
            }
            return Boolean.compare(r2.getLiked(), r1.getLiked());
        });

        recruitApplications.forEach(recruitApplicationElement ->
                this.recruitApplications.add(new RecruitApplicationResElement(recruitApplicationElement)));
    }
}
