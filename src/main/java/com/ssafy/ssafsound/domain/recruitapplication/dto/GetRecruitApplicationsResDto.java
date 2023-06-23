package com.ssafy.ssafsound.domain.recruitapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRecruitApplicationsResDto {

    private List<RecruitApplicationElement> recruitApplications;
}
