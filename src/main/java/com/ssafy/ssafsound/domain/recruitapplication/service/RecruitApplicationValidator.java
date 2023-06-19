package com.ssafy.ssafsound.domain.recruitapplication.service;

import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;

public interface RecruitApplicationValidator {
    boolean hasError(RecruitApplication recruitApplication, Long memberId) throws RecruitException;
}
