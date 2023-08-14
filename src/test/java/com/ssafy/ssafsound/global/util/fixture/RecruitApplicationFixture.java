package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitApplicationsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitParticipantsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;

import java.util.Arrays;
import java.util.List;

public class RecruitApplicationFixture {
    public static final PostRecruitApplicationReqDto POST_RECRUIT_APPLICATION_REQ_DTO
            = new PostRecruitApplicationReqDto(RecruitType.FRONT_END.getName(), List.of("취업 준비를 위해서 신청하게되었습니다."));

    public static final RecruitApplication getRecruitApplication1_ByMatchStatus(MatchStatus matchStatus, MetaData recruitType, Boolean isLike) {
        return RecruitApplication.builder()
                .id(1L)
                .matchStatus(matchStatus)
                .recruit(RecruitFixture.RECRUIT_1)
                .type(recruitType)
                .member(MemberFixture.MEMBER_TIM)
                .isLike(isLike)
                .build();
    }

    public static final RecruitApplicationElement APPLICATION1_ELEMENT = RecruitApplicationElement.builder()
            .recruitApplicationId(1L)
            .recruitType(RecruitType.FRONT_END.getName())
            .matchStatus(MatchStatus.WAITING_REGISTER_APPROVE)
            .memberId(MemberFixture.MEMBER_TIM.getId())
            .nickname(MemberFixture.MEMBER_TIM.getNickname())
            .isMajor(MemberFixture.MEMBER_TIM.getMajor())
            .ssafyInfo(SSAFYInfo.from(MemberFixture.MEMBER_TIM))
            .question(RecruitFixture.RECRUIT_1.getQuestions().get(0).getContent())
            .reply("취업 준비를 위해서 신청하게되었습니다.")
            .isLike(false)
            .build();

    public static final GetRecruitApplicationsResDto GET_RECRUIT_APPLICATIONS_RES_DTO = new GetRecruitApplicationsResDto(Arrays.asList(APPLICATION1_ELEMENT));

    public static final GetRecruitParticipantsResDto GET_RECRUIT_PARTICIPANTS_RES_DTO = GetRecruitParticipantsResDto
            .of(List.of(getRecruitApplication1_ByMatchStatus(MatchStatus.WAITING_REGISTER_APPROVE, new MetaData(RecruitType.FRONT_END), false)),List.of(RecruitFixture.RECRUIT_1_BE_LIMIT_3, RecruitFixture.RECRUIT_1_FE_LIMIT_3));

    static {
        GET_RECRUIT_PARTICIPANTS_RES_DTO.addRegisterInfo(RecruitFixture.RECRUIT_1);
    }
}