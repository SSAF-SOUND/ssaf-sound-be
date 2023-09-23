package com.ssafy.ssafsound.domain.recruit.dto;

import java.util.List;
import java.util.Map;

public interface AddParticipantDto {
    List<Long> getRecruitsId();
    Map<Long, Map<String, RecruitParticipant>> getRecruitParticipantMapByRecruitIdAndRecruitType();
}
