package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GetRecruitParticipantsResDto {
    List<RecruitParticipantElement> members;

    public static GetRecruitParticipantsResDto from(List<RecruitApplication> recruitApplications) {
        return new GetRecruitParticipantsResDto(recruitApplications.stream()
                .map(RecruitParticipantElement::from)
                .collect(Collectors.toList()));
    }
}
