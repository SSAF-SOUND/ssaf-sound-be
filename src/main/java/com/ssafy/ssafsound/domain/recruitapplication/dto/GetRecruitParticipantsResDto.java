package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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
            Long recruitApplicationId = recruitApplication.getId();
            LocalDateTime modifiedAt = recruitApplication.getModifiedAt();
            String recruitType = recruitApplication.getType().getName();
            participantElementMap.get(recruitType).addMember(recruitApplicationId, modifiedAt, recruitApplication.getMember());
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

        // 리크루트 등록자는 별도의 리크루트 신청이 없으며, 리크루트 글 등록일이 참여 확정일이다.
        recruitParticipantElement.addMember(-1L, recruit.getCreatedAt(), recruit.getMember());
        recruitParticipantElement.increaseLimit();
    }
}
