package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitApplicationStatusResDto;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.*;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitApplicationLikeResDto;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class RecruitApplicationFixture {
    public static final PostRecruitApplicationReqDto POST_RECRUIT_APPLICATION_REQ_DTO
            = new PostRecruitApplicationReqDto(RecruitType.FRONT_END.getName(), List.of("취업 준비를 위해서 신청하게되었습니다."));

    public static final PostRecruitApplicationLikeResDto POST_RECRUIT_APPLICATION_LIKE_RES_DTO = new PostRecruitApplicationLikeResDto(true);
    public static final RecruitApplication getRecruitApplication1_ByMatchStatus(MatchStatus matchStatus, MetaData recruitType, Boolean isLike) {
        RecruitApplication recruitApplication = RecruitApplication.builder()
                .id(1L)
                .matchStatus(matchStatus)
                .recruit(RecruitFixture.RECRUIT_1)
                .type(recruitType)
                .member(MemberFixture.MEMBER_TIM)
                .isLike(isLike)
                .build();

        ReflectionTestUtils.setField(recruitApplication, BaseTimeEntity.class,"createdAt", LocalDateTime.now(), LocalDateTime.class);
        ReflectionTestUtils.setField(recruitApplication, BaseTimeEntity.class,"modifiedAt", LocalDateTime.now(), LocalDateTime.class);
        return recruitApplication;
    }

    public static final RecruitApplicationElement APPLICATION1_ELEMENT = RecruitApplicationElement.builder()
            .recruitId(1L)
            .recruitApplicationId(1L)
            .recruitType(RecruitType.FRONT_END.getName())
            .matchStatus(MatchStatus.PENDING)
            .author(new AuthorElement(MemberFixture.MEMBER_TIM, false))
            .question(RecruitFixture.RECRUIT_1.getQuestions().get(0).getContent())
            .reply("취업 준비를 위해서 신청하게되었습니다.")
            .liked(false)
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .build();

    public static final GetRecruitApplicationDetailResDto APPLICATION_DETAIL_RES_DTO = new GetRecruitApplicationDetailResDto(APPLICATION1_ELEMENT);

    public static final GetRecruitApplicationsResDto GET_RECRUIT_APPLICATIONS_RES_DTO = new GetRecruitApplicationsResDto(RecruitFixture.RECRUIT_1, Arrays.asList(APPLICATION1_ELEMENT));

    public static final GetRecruitParticipantsResDto GET_RECRUIT_PARTICIPANTS_RES_DTO = GetRecruitParticipantsResDto
            .of(List.of(getRecruitApplication1_ByMatchStatus(MatchStatus.DONE, new MetaData(RecruitType.FRONT_END), false)),List.of(RecruitFixture.RECRUIT_1_BE_LIMIT_3, RecruitFixture.RECRUIT_1_FE_LIMIT_3));

    public static final PatchRecruitApplicationStatusResDto WAITING_STATUS_APPLICATION = new PatchRecruitApplicationStatusResDto(1L, MatchStatus.PENDING.name());

    public static final PatchRecruitApplicationStatusResDto REJECT_STATUS_APPLICATION = new PatchRecruitApplicationStatusResDto(1L, MatchStatus.REJECT.name());

    public static final PatchRecruitApplicationStatusResDto CANCEL_STATUS_APPLICATION = new PatchRecruitApplicationStatusResDto(1L, MatchStatus.CANCEL.name());

    public static final PatchRecruitApplicationStatusResDto DONE_STATUS_APPLICATION = new PatchRecruitApplicationStatusResDto(1L, MatchStatus.DONE.name());

    static {
        GET_RECRUIT_PARTICIPANTS_RES_DTO.addRegisterInfo(RecruitFixture.RECRUIT_1);
    }
}
