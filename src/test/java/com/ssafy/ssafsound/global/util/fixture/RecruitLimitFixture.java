package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;

public class RecruitLimitFixture {
    public static final RecruitLimitElement STUDY_LIMIT_3 = new RecruitLimitElement(RecruitType.STUDY.getName(), 3);
    public static final RecruitLimitElement BACKEND_LIMIT_3 = new RecruitLimitElement(RecruitType.BACK_END.getName(), 3);
    public static final RecruitLimitElement FRONTEND_LIMIT_3 = new RecruitLimitElement(RecruitType.FRONT_END.getName(), 3);
    public static final RecruitLimitElement DESIGN_LIMIT_3 = new RecruitLimitElement(RecruitType.DESIGN.getName(), 3);
    public static final RecruitLimitElement APP_LIMIT_3 = new RecruitLimitElement(RecruitType.APP.getName(), 3);
}
