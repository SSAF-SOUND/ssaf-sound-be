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
    }
}
