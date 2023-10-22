package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.meta.domain.Skill;
import com.ssafy.ssafsound.domain.recruit.domain.*;
import com.ssafy.ssafsound.domain.recruit.dto.*;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecruitFixture {

    private static final MemberFixture memberFixture = new MemberFixture();
    private static String CONTACT_URI = "https://open.kakao.com/o/sA8Kb83b";
    private static String RECRUIT_TITLE = "[사이드 프로젝트] 공모전 레퍼런스 웹 플랫폼 개발 프로젝트 팀원을 모집합니다.";
    private static String RECRUIT_CONTENT = "<p>[프로젝트 소개]</p>\n" +
            "저희 프로젝트는 대학생들의 공모전 경험에 있어 누구나 참고할 레퍼런스를 제공받고, 또 자발적으로 공유할 수 있는 환경을 구축하고자 시작하게 되었습니다! 저희 프로젝트는 아래와 같은 특징을 가지고 있습니다.\n" +
            "현재 PM 1명, 프론트엔드 개발자 2명, 백엔드 개발자 1명이 있으며, 디자인의 경우 PM이 1차 MVP의 UI/UX를 제작했습니다.\n";
    private static String RECRUIT_QUESTION = "프로젝트에 참여하고자 하는 동기가 무엇인가요?";

    private static LocalDate startDate = LocalDate.now();
    private static LocalDate endDate = LocalDate.now().plusDays(7);

    public static final PostRecruitReqDto RECRUIT_POST_REQ_DTO = new PostRecruitReqDto(
            Category.PROJECT.name(),
            endDate,
            RECRUIT_TITLE,
            RECRUIT_CONTENT,
            CONTACT_URI,
            RecruitType.BACK_END.getName(),
            ProductionMetaDataNameFixture.skills,
            List.of(RECRUIT_QUESTION), List.of(RecruitLimitFixture.BACKEND_LIMIT_3, RecruitLimitFixture.FRONTEND_LIMIT_3, RecruitLimitFixture.APP_LIMIT_3)
    );

    public static final PostRecruitScrapCountResDto RECRUIT_SCRAP_RES_DTO = new PostRecruitScrapCountResDto(1L, true);

    public static final Recruit RECRUIT_1 = Recruit.builder()
            .id(1L)
            .view(1L)
            .category(Category.PROJECT)
            .title(RECRUIT_TITLE)
            .content(RECRUIT_CONTENT)
            .startDateTime(startDate.atStartOfDay())
            .endDateTime(endDate.atTime(LocalTime.MAX))
            .deletedRecruit(false)
            .finishedRecruit(false)
            .registerRecruitType(new MetaData(RecruitType.BACK_END))
            .contactURI(CONTACT_URI)
            .member(memberFixture.createMember())
            .build();

    static {
        ReflectionTestUtils.setField(RECRUIT_1, BaseTimeEntity.class,"createdAt", LocalDateTime.now(), LocalDateTime.class);
        ReflectionTestUtils.setField(RECRUIT_1, BaseTimeEntity.class,"modifiedAt", LocalDateTime.now(), LocalDateTime.class);
    }
    public static final RecruitQuestion RECRUIT_QUESTION_1 = RecruitQuestion.builder()
            .id(1L)
            .content(RECRUIT_QUESTION)
            .recruit(RECRUIT_1)
            .build();

    public static final RecruitSkill RECRUIT_1_SKILL_SPRING = RecruitSkill.builder()
            .id(1L)
            .recruit(RECRUIT_1)
            .skill(new MetaData(Skill.SPRING))
            .build();

    public static final RecruitSkill RECRUIT_1_SKILL_REACT = RecruitSkill.builder()
            .id(2L)
            .recruit(RECRUIT_1)
            .skill(new MetaData(Skill.REACT))
            .build();

    public static final RecruitLimitation RECRUIT_1_BE_LIMIT_3 = RecruitLimitation.builder()
            .id(1L)
            .currentNumber(0)
            .limitation(3)
            .recruit(RECRUIT_1)
            .type(new MetaData(RecruitType.BACK_END))
            .build();

    public static final RecruitLimitation RECRUIT_1_FE_LIMIT_3 = RecruitLimitation.builder()
            .id(2L)
            .currentNumber(0)
            .limitation(3)
            .recruit(RECRUIT_1)
            .type(new MetaData(RecruitType.FRONT_END))
            .build();
    public static final List<RecruitSkillElement> RECRUIT_1_SKILL_DTO = List.of(
            RecruitSkillElement.builder().skillId(Skill.SPRING.getId()).name(Skill.SPRING.getName()).build(),
            RecruitSkillElement.builder().skillId(Skill.REACT.getId()).name(Skill.REACT.getName()).build());

    // registerRecruitType 반영으로 인해 BE current Number = 1
    public static final List<GetRecruitLimitDetail> RECRUIT_1_LIMIT_DETAIL = List.of(
        new GetRecruitLimitDetail(RECRUIT_1_BE_LIMIT_3.getType().getName(), RECRUIT_1_BE_LIMIT_3.getLimitation(), 1),
        new GetRecruitLimitDetail(RECRUIT_1_FE_LIMIT_3.getType().getName(), RECRUIT_1_FE_LIMIT_3.getLimitation(), 0)
    );

    public static final GetRecruitDetailResDto RECRUIT_1_DETAIL_RES_DTO = GetRecruitDetailResDto.builder()
            .category(Category.PROJECT.name())
            .recruitId(RECRUIT_1.getId())
            .title(RECRUIT_1.getTitle())
            .content(RECRUIT_1.getContent())
            .contactURI(RECRUIT_1.getContactURI())
            .view(RECRUIT_1.getView())
            .finishedRecruit(RECRUIT_1.getFinishedRecruit())
            .recruitStart(RECRUIT_1.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .recruitEnd(RECRUIT_1.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .skills(RECRUIT_1_SKILL_DTO)
            .limits(RECRUIT_1_LIMIT_DETAIL)
            .questions(List.of(RECRUIT_QUESTION))
            .author(new AuthorElement(memberFixture.createMember(), false))
            .scrapCount(1L)
            .scraped(true)
            .mine(true)
            .matchStatus(MatchStatus.INITIAL.name())
            .build();

    private static final List<RecruitLimitElement> RECRUIT_1_UPDATE_LIMIT = List.of(
            new RecruitLimitElement(RECRUIT_1_BE_LIMIT_3.getType().getName(), 5),
            new RecruitLimitElement(RECRUIT_1_FE_LIMIT_3.getType().getName(), 5)
    );

    public static final PatchRecruitReqDto RECRUIT_1_PATCH_REQ_DTO = PatchRecruitReqDto.builder()
            .category("PROJECT")
            .registerRecruitType(RecruitType.BACK_END.getName())
            .recruitEnd(LocalDate.parse("2025-12-01"))
            .title("제목 수정")
            .content("<p>컨텐츠 수정입니다. </p>")
            .contactURI("github.com")
            .skills(List.of(Skill.REACT.getName(), Skill.SPRING.getName(), Skill.SVELTE.getName()))
            .limitations(RECRUIT_1_UPDATE_LIMIT)
            .build();

    static {
        RECRUIT_1.setRecruitQuestions(List.of(RECRUIT_QUESTION_1));
        RECRUIT_1.setRecruitSkill(List.of(RECRUIT_1_SKILL_SPRING, RECRUIT_1_SKILL_REACT));
        RECRUIT_1.setRecruitLimitations(List.of(RECRUIT_1_BE_LIMIT_3, RECRUIT_1_FE_LIMIT_3));
    }

    private static final RecruitElement RECRUIT_1_ELEMENT = RecruitElement.fromRecruitAndLoginMemberId(RECRUIT_1, 1L);

    public static final GetRecruitsCursorResDto GET_RECRUITS_CURSOR_RES_DTO = GetRecruitsCursorResDto.builder()
            .recruits(List.of(RECRUIT_1_ELEMENT))
            .nextCursor(1L)
            .isLast(true)
            .build();

    public static final GetRecruitOffsetResDto GET_RECRUITS_OFFSET_RES_DTO = GetRecruitOffsetResDto.builder()
            .recruits(List.of(RECRUIT_1_ELEMENT))
            .currentPage(0)
            .totalPageCount(1)
            .build();

    private static final RecruitApplicationElement APPLICATION_ELEMENT = RecruitApplicationFixture.APPLICATION1_ELEMENT();
    private static final AppliedRecruit APPLIED_RECRUIT = AppliedRecruit.builder()
            .recruit(RECRUIT_1)
            .appliedAt(APPLICATION_ELEMENT.getCreatedAt())
            .matchStatus(APPLICATION_ELEMENT.getMatchStatus())
            .build();
    private static final AppliedRecruitElement APPLIED_RECRUIT_ELEMENT = AppliedRecruitElement.fromRecruitAndLoginMemberId(APPLIED_RECRUIT, 100L);

    public static final GetMemberAppliedRecruitsResDto GET_MEMBER_APPLIED_RECRUITS_RES_DTO = GetMemberAppliedRecruitsResDto.builder()
            .recruits(List.of(APPLIED_RECRUIT_ELEMENT))
            .nextCursor(1L)
            .isLast(true)
            .build();
}
