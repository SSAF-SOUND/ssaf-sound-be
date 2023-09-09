package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.*;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.*;
import com.ssafy.ssafsound.domain.recruit.dto.*;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitSkillRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitScrapRepository;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @Mock
    private RecruitRepository recruitRepository;

    @Mock
    private RecruitLimitationRepository recruitLimitationRepository;

    @Mock
    private RecruitSkillRepository recruitSkillRepository;

    @Mock
    private RecruitApplicationRepository recruitApplicationRepository;

    @Mock
    private RecruitScrapRepository recruitScrapRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MetaDataConsumer metaDataConsumer;

    @InjectMocks
    private RecruitService recruitService;

    private Member member = Member.builder()
            .id(1L)
            .nickname("khs")
            .semester(9)
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .role(MemberRole.builder().id(1).roleType("user").build())
            .major(true)
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .campus(new MetaData(Campus.SEOUL))
            .build();

    private Member participant = Member.builder()
            .id(2L)
            .nickname("kds")
            .semester(9)
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .role(MemberRole.builder().id(1).roleType("user").build())
            .major(true)
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .campus(new MetaData(Campus.SEOUL))
            .build();

    private Recruit recruit = Recruit.builder()
            .id(1L)
            .category(Category.PROJECT)
            .build();

    private Recruit savedRecruit = Recruit.builder()
            .id(2L)
            .category(Category.PROJECT)
            .view(0L)
            .member(member)
            .title("제목")
            .content("컨텐츠")
            .registerRecruitType(new MetaData(RecruitType.BACK_END))
            .deletedRecruit(false)
            .startDateTime(LocalDate.now().atStartOfDay())
            .endDateTime(LocalDate.now().plusDays(3).atTime(LocalTime.MAX))
            .build();

    private List<RecruitLimitation> limits = List.of(RecruitLimitation.builder()
            .recruit(savedRecruit)
            .limitation(2)
            .currentNumber(0)
            .type(new MetaData(RecruitType.DESIGN))
            .build());

    private List<RecruitSkill> skills = List.of(RecruitSkill.builder()
            .recruit(savedRecruit)
            .id(1L)
            .recruit(savedRecruit)
            .skill(new MetaData(RecruitType.BACK_END))
            .build());

    private RecruitScrap recruitScrap = RecruitScrap.builder().member(member).recruit(recruit).build();

    private RecruitApplication recruitApplication = RecruitApplication.builder()
            .id(1L)
            .isLike(false)
            .matchStatus(MatchStatus.DONE)
            .recruit(savedRecruit)
            .type(new MetaData(RecruitType.DESIGN))
            .member(participant)
            .build();

    private final PostRecruitReqDto postRecruitReqDto = new PostRecruitReqDto(
            Category.STUDY.name(), LocalDate.now(),  "스터디/프로젝트 모집 제목", "컨텐츠",
            "contact/uri",
            RecruitType.DESIGN.getName(), Arrays.stream(Skill.values()).map(Skill::getName).collect(Collectors.toList()),
            Collections.singletonList("프로젝트/스터디 등록자가 참여자에게 묻고 싶은 자유 질문"),
            Arrays.stream(RecruitType.values()).map(recruitType-> new RecruitLimitElement(recruitType.getName(), 2)).collect(Collectors.toList()));

    private final Pageable pageInfo = PageRequest.of(0, 10);

    private final GetRecruitsReqDto emptyKeywordDto = GetRecruitsReqDto.builder()
            .category(Category.PROJECT.name())
            .keyword("")
            .build();

    private final GetRecruitsReqDto findTitleDto = GetRecruitsReqDto.builder()
            .category(Category.PROJECT.name())
            .keyword("제목")
            .build();

    private final GetRecruitsReqDto notFindKeywordDto = GetRecruitsReqDto.builder()
            .category(Category.PROJECT.name())
            .keyword("없는거")
            .build();

    @BeforeEach
    void setStub() {
        savedRecruit.setRecruitLimitations(limits);
        savedRecruit.setRecruitSkill(skills);

        // Repository Mocking
        lenient().when(memberRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(member));
        lenient().when(memberRepository.findById(2L)).thenThrow(new RuntimeException());

        lenient().when(recruitScrapRepository.findByRecruitIdAndMemberId(1L, 1L)).thenReturn(java.util.Optional.ofNullable(recruitScrap));
        lenient().when(recruitRepository.findByIdUsingFetchJoinRegisterAndRecruitLimitation(2L))
                .thenReturn(java.util.Optional.ofNullable(savedRecruit));
        lenient().when(recruitRepository.findByIdUsingFetchJoinRegister(2L))
                .thenReturn(java.util.Optional.ofNullable(savedRecruit));
        lenient().when(recruitRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(savedRecruit));

        lenient().when(recruitRepository
                .findRecruitByGetRecruitsReqDto(emptyKeywordDto, pageInfo))
                .thenReturn(new SliceImpl<>(List.of(savedRecruit)));

        lenient().when(recruitRepository.findRecruitByGetRecruitsReqDto(findTitleDto, pageInfo))
                .thenReturn(new SliceImpl<>(List.of(savedRecruit)));

        lenient().when(recruitRepository.findRecruitByGetRecruitsReqDto(notFindKeywordDto, pageInfo))
                .thenReturn(new SliceImpl<>(List.of()));


        lenient().when(recruitApplicationRepository.findDoneRecruitApplicationByRecruitIdInFetchRecruitAndMember(List.of(2L)))
                .thenReturn(List.of(recruitApplication));
        lenient().when(recruitApplicationRepository.findByRecruitIdAndMatchStatus(2L, MatchStatus.DONE))
                .thenReturn(List.of(recruitApplication));


        // MetaData Consumer Mocking
        Arrays.stream(RecruitType.values()).forEach(recruitType ->lenient()
                .when(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType.getName()))
                .thenReturn(new MetaData(recruitType)));

        Arrays.stream(Skill.values()).forEach(skill -> lenient()
                .when(metaDataConsumer.getMetaData(MetaDataType.SKILL.name(), skill.getName()))
                .thenReturn(new MetaData(skill)));

        Arrays.stream(MajorTrack.values()).forEach(majorTrack -> lenient()
                .when(metaDataConsumer.getMetaData(MetaDataType.MAJOR_TRACK.name(), majorTrack.getName()))
                .thenReturn(new MetaData(majorTrack)));
    }

    @DisplayName("토큰과 정상 리크루트글 등록 요청이 넘어온 경우 리크루트 글 등록 성공")
    @Test
    void Given_AuthenticatedMemberAndPostRecruitReqDto_When_SaveRecruitService_Then_Success() {
        AuthenticatedMember existUser = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        Recruit recruit = recruitService.saveRecruit(existUser.getMemberId(), postRecruitReqDto);

        assertAll(
                ()-> assertEquals(member, recruit.getMember()),
                ()-> {
                    assertEquals( 0, recruit.getApplications().size());
                    assertEquals(RecruitType.DESIGN.getName(), recruit.getRegisterRecruitType().getName());
                },
                ()-> {
                    List<RecruitLimitation> recruitLimitations = recruit.getLimitations();
                    assertNotNull(recruitLimitations);
                    assertEquals(RecruitType.values().length, recruitLimitations.size());

                    for(RecruitLimitation recruitLimitation: recruitLimitations) {
                        assertEquals(2, recruitLimitation.getLimitation());
                        assertEquals(0, recruitLimitation.getCurrentNumber());
                    }
                },
                ()-> {
                    List<RecruitSkill> skills = recruit.getSkills();
                    Skill[] allSkill = Skill.values();
                    int len = skills.size();
                    for(int i=0; i<len; ++i) {
                        assertEquals(allSkill[i].getName(), skills.get(i).getSkill().getName());
                    }
                }
        );
    }

    @DisplayName("토큰으로부터 얻은 사용자 정보가 유효하지 않은 경우 리크루트글 등록 실패")
    @Test
    void Given_EmptyAuthenticatedMemberAndPostRecruitReqDto_When_SaveRecruitService_Then_ThrowException() {
        AuthenticatedMember notExistMember = AuthenticatedMember.builder()
                .memberId(2L)
                .build();

        assertThrows(RuntimeException.class, ()-> recruitService.saveRecruit(notExistMember.getMemberId(), postRecruitReqDto));
    }

    @DisplayName("사용자 리크루팅 스크랩 등록")
    @Test
    void Given_MemberIdAndRecruitId_When_TryToggleRecruitScrap_Then_InsertRecruitScrap() {
        Long recruitId = 2L, memberId = 1L;
        recruitService.toggleRecruitScrap(recruitId, memberId);
        verify(recruitScrapRepository).findByRecruitIdAndMemberId(2L, 1L);
        verify(recruitScrapRepository).countByRecruitId(2L);
    }

    @DisplayName("사용자 리크루팅 스크랩 취소(토글)")
    @Test
    void Given_MemberIdAndRecruitId_When_TryToggleRecruitScrap_Then_DeleteRecruitScrap() {
        Long recruitId = 1L, memberId = 1L;
        recruitService.toggleRecruitScrap(recruitId, memberId);
    }

    @DisplayName("등록자의 리크루트 타입이 인원제한에 포함되지 않은 리크루트 상세 조회")
    @Test
    void Given_NotIncludeRegisterRecruitTypeRecruitId_When_GetRecruitDetail_Then_Success() {
        GetRecruitDetailResDto dto = recruitService.getRecruitDetail(2L, 2L);
        String registerRecruitType = savedRecruit.getRegisterRecruitType().getName();

        assertAll(
                ()->assertEquals(1L, dto.getView()),
                ()->assertEquals(false, dto.isFinishedRecruit()),
                ()-> dto.getLimits().forEach(limit->{
                    if(limit.getRecruitType().equals(registerRecruitType)) {
                        assertEquals(1, limit.getLimit());
                    } else {
                        assertEquals(2, limit.getLimit());
                    }
                })
        );
    }

    @DisplayName("등록자의 리크루트 타입이 인원제한에 포함되지 않은 리크루트 상세 조회")
    @Test
    void Given_RecruitId_When_GetRecruitDetail_Then_Success() {
        savedRecruit.setRegisterRecruitType(new MetaData(RecruitType.DESIGN));
        GetRecruitDetailResDto dto = recruitService.getRecruitDetail(2L, 2L);

        assertAll(
                ()->assertEquals(1L, dto.getView()),
                ()->assertEquals(false, dto.isFinishedRecruit()),
                ()-> dto.getLimits().forEach(limit-> assertEquals(3, limit.getLimit()))
        );
    }

    @DisplayName("존재하지 않는 리크루트에 대한 상세 조회 실패")
    @Test
    void Given_NotExistRecruitId_When_GetRecruitDetail_Then_Fail() {
        assertThrows(ResourceNotFoundException.class, ()->recruitService.getRecruitDetail(1L, 2L));
    }

    @DisplayName("삭제된 리크루트 상세 조회시 null값 return")
    @Test
    void Given_DeletedRecruitId_When_GetRecruitDetail_Then_Fail() {
        savedRecruit.delete();
        assertThrows(RecruitException.class, ()->recruitService.getRecruitDetail(2L, 2L));
    }

    @DisplayName("리크루트 삭제")
    @Test
    void Given_RecruitIdAndRegisterId_When_DeleteRecruit_Then_Success() {
        assertAll(
                ()->assertDoesNotThrow(()-> recruitService.deleteRecruit(2L, 1L)),
                ()->assertEquals(true, savedRecruit.getDeletedRecruit())
        );
    }

    @DisplayName("등록자가 아닌 사용자의 리크루트 삭제 요청 실패")
    @Test
    void Given_NotValidRegisterId_When_DeleteRecruit_Then_Success() {
        assertThrows(RecruitException.class, ()->recruitService.deleteRecruit(2L, 2L));
    }

    @DisplayName("리크루트 수정")
    @Test
    void Given_RecruitIdAndRegisterIdAndPatchRecruitDto_When_DeleteRecruit_Then_Success() {
        List<RecruitLimitElement> limits = List.of(
                new RecruitLimitElement(RecruitType.BACK_END.getName(), 3),
                new RecruitLimitElement(RecruitType.DESIGN.getName(), 3)
        );

        List<String> skills = Arrays.stream(Skill.values()).map(Skill::getName).collect(Collectors.toList());

        PatchRecruitReqDto patchRecruitReqDto = new PatchRecruitReqDto("PROJECT", RecruitType.BACK_END.getName(),
                LocalDate.now(), "제목 수정", "컨텐츠 수정", "open.kakao.uri", skills, limits);

        recruitService.updateRecruit(2L, 1L, patchRecruitReqDto);

        assertAll(
                ()-> assertEquals("제목 수정", savedRecruit.getTitle()),
                ()-> assertEquals("컨텐츠 수정", savedRecruit.getContent()),
                ()-> assertEquals(2, savedRecruit.getLimitations().size()),
                ()-> assertEquals(RecruitType.BACK_END.getName(), savedRecruit.getRegisterRecruitType().getName())
        );
    }

    @DisplayName("기존에 모집완료된 인원이 있는 모집군을 삭제하는 리크루트 수정 실패")
    @Test
    void Given_NotIncludePrevExistLimitAndPatchRecruitDto_When_DeleteRecruit_Then_Fail() {
        List<RecruitLimitElement> limits = List.of(
                new RecruitLimitElement(RecruitType.BACK_END.getName(), 3)
        );

        List<String> skills = Arrays.stream(Skill.values()).map(Skill::getName).collect(Collectors.toList());

        PatchRecruitReqDto patchRecruitReqDto = new PatchRecruitReqDto("PROJECT", RecruitType.BACK_END.getName(),
                LocalDate.now(), "제목 수정", "컨텐츠 수정","open.kakao.uri", skills, limits);

        RecruitException recruitException = assertThrows(RecruitException.class, () -> recruitService.updateRecruit(2L, 1L, patchRecruitReqDto));
        assertEquals(recruitException.getInfo(), RecruitErrorInfo.NOT_BELOW_PREV_LIMITATIONS);
    }

    @DisplayName("키워드를 입력하지 않은 리크루트 목록 검색")
    @Test
    void Given_NotIncludeKeyword_When_GetPagingRecruits_Then_Success() {
        GetRecruitsResDto getRecruitsResDto = recruitService.getRecruits(emptyKeywordDto, pageInfo, null);
        assertEquals(1, getRecruitsResDto.getRecruits().size());
    }

    @DisplayName("키워드를 입력한 리크루트 목록 검색 (검색 결과 O)")
    @Test
    void Given_IncludeKeyword_When_GetPagingRecruits_Then_Success() {
        GetRecruitsResDto getRecruitsResDto = recruitService.getRecruits(findTitleDto, pageInfo, null);
        assertEquals(1, getRecruitsResDto.getRecruits().size());
    }

    @DisplayName("인원제한이 설정된 모집군과 등록자의 모집군이 일치하는 리크루트 목록 검색")
    @Test
    void Given_IncludeKeywordAndIncludeRegisterRecruitType_When_GetPagingRecruits_Then_Success() {
        savedRecruit.setRegisterRecruitType(new MetaData(RecruitType.DESIGN));

        GetRecruitsResDto getRecruitsResDto = recruitService.getRecruits(findTitleDto, pageInfo, null);
        assertEquals(1, getRecruitsResDto.getRecruits().size());
        assertEquals(1, getRecruitsResDto.getRecruits().get(0).getParticipants().size());
        assertEquals(3, getRecruitsResDto.getRecruits().get(0).getParticipants().get(0).getLimit());
        assertEquals(2, getRecruitsResDto.getRecruits().get(0).getParticipants().get(0).getMembers().size());
    }


    @DisplayName("키워드를 입력한 리크루트 목록 검색 (검색 결과 X)")
    @Test
    void Given_IncludeKeyword_When_GetPagingRecruits_Then_EmptySet() {
        GetRecruitsResDto getRecruitsResDto = recruitService.getRecruits(notFindKeywordDto, pageInfo, null);
        assertEquals(0, getRecruitsResDto.getRecruits().size());
    }

    @DisplayName("등록자 리크루트 완료 성공")
    @Test
    void Given_RecruitId_When_ExpiredRecruit_Then_Success() {
        recruitService.expiredRecruit(2L, 1L);
        assertEquals(true, savedRecruit.getFinishedRecruit());
        verify(recruitRepository).findByIdUsingFetchJoinRegister(2L);
    }

    @DisplayName("등록자가 아닌 사용자의 리크루트 완료 요쳥")
    @Test
    void Given_RecruitId_When_ExpiredRecruit_Then_Fail() {
        RecruitException recruitException = assertThrows(
                RecruitException.class,
                () -> recruitService.expiredRecruit(2L, 2L));

        assertEquals(
                RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION.getCode(),
                recruitException.getInfo().getCode()
        );
    }

    @DisplayName("사용자 프로필 - 사용자가 참가한 리크루트 목록 조회")
    @Test
    void Given_MemberId_When_GetMemberJoinRecruits_Then_Success() {
        // TODO Test Refactoring 시 일괄 작성
    }
}