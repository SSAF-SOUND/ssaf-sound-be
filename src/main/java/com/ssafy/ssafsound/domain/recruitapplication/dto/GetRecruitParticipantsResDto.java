package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GetRecruitParticipantsResDto {
    private Map<String, RecruitParticipantElement> recruitTypes;

    public static GetRecruitParticipantsResDto of(List<RecruitApplication> recruitApplications, List<RecruitLimitation> recruitLimitations) {
        Map<String, RecruitParticipantElement> participantElementMap = new HashMap<>();

        recruitLimitations.forEach(recruitLimitation -> {
            String recruitType = recruitLimitation.getType().getName();
            participantElementMap.put(recruitType, new RecruitParticipantElement(recruitLimitation.getLimitation()));
        });

        recruitApplications.forEach(recruitApplication -> {
            String recruitType = recruitApplication.getType().getName();
            participantElementMap.get(recruitType).addMember(recruitApplication.getMember());
        });

        return new GetRecruitParticipantsResDto(participantElementMap);
    }

    public void addRegisterInfo(Recruit recruit) {
        String recruitType = recruit.getRegisterRecruitType().getName();
        RecruitParticipantElement recruitParticipantElement = recruitTypes.get(recruitType);
        if(recruitParticipantElement == null) {
            recruitParticipantElement = new RecruitParticipantElement(0);
            recruitTypes.put(recruitType, recruitParticipantElement);
        }

        recruitParticipantElement.addMember(recruit.getMember());
        recruitParticipantElement.increaseLimit();
    }
}
