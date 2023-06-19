package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;

public interface RecruitApplicationValidator {
    boolean hasError(RecruitApplication recruitApplication, Long memberId) throws RecruitException;
}
