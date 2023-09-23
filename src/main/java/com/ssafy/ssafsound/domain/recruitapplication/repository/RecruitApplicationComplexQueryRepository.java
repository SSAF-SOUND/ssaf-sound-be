package com.ssafy.ssafsound.domain.recruitapplication.repository;

import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;

public interface RecruitApplicationComplexQueryRepository {
    RecruitApplicationElement findTopByRecruitIdAndMemberId(Long recruitId, Long memberId);
}
