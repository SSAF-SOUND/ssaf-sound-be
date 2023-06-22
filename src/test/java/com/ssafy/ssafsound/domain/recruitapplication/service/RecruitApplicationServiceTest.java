package com.ssafy.ssafsound.domain.recruitapplication.service;

import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.MajorType;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestion;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitQuestionReplyRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecruitApplicationServiceTest {

    @Mock
    private RecruitRepository recruitRepository;

    @Mock
    private RecruitApplicationRepository recruitApplicationRepository;

    @Mock
    private RecruitQuestionReplyRepository recruitQuestionReplyRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MetaDataConsumer metaDataConsumer;

    @InjectMocks
    private RecruitApplicationService recruitApplicationService;

    Member register = Member.builder()
            .id(1L)
            .nickname("khs")
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .major(true)
            .majorType(new MajorType(1, "자바전공"))
            .build();

    Member participant = Member.builder()
            .id(2L)
            .nickname("kds")
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .major(false)
            .majorType(null)
            .build();

    Recruit recruit = Recruit.builder()
            .id(1L)
            .member(register)
            .title("스터디/리크루트 모집 제목")
            .content("컨텐츠")
            .build();

    RecruitQuestion recruitQuestion = RecruitQuestion.builder()
            .id(1L)
            .recruit(recruit)
            .content("질문")
            .build();

    RecruitApplication recruitApplication = RecruitApplication.builder()
            .id(1L)
            .matchStatus(MatchStatus.WAITING_REGISTER_APPROVE)
            .recruit(recruit)
            .type(new MetaData(RecruitType.DESIGN))
            .member(participant)
            .build();

    List<RecruitLimitation> limits = List.of(RecruitLimitation.builder()
            .recruit(recruit)
            .limitation(2)
            .currentNumber(0)
            .type(new MetaData(RecruitType.DESIGN))
            .build());

    List<RecruitApplication> recruitApplications = List.of(
            RecruitApplication.builder()
                    .matchStatus(MatchStatus.DONE)
                    .member(register)
                    .type(new MetaData(RecruitType.DESIGN))
                    .recruit(recruit)
                    .build(),
            RecruitApplication.builder()
                    .matchStatus(MatchStatus.DONE)
                    .member(participant)
                    .type(new MetaData(RecruitType.DESIGN))
                    .recruit(recruit)
                    .build()
    );
    @BeforeEach
    void setUpStubAndFixture() {
        // SetUp Fixture
        recruit.setRecruitLimitations(limits);
        recruit.setRecruitQuestions(List.of(recruitQuestion));

        // Repository Mocking
        Mockito.lenient().when(recruitRepository.findByIdUsingFetchJoinRecruitLimitation(1L))
                .thenReturn(java.util.Optional.ofNullable(recruit));
        Mockito.lenient().when(recruitApplicationRepository.findByIdAndMemberId(1L, 2L))
                .thenReturn(java.util.Optional.ofNullable(recruitApplication));
        Mockito.lenient().when(recruitApplicationRepository.findByIdFetchRecruitWriter(1L))
                .thenReturn(java.util.Optional.ofNullable(recruitApplication));
        Mockito.lenient().when(recruitApplicationRepository.findByIdAndMemberIdFetchRecruitWriter(1L))
                .thenReturn(java.util.Optional.ofNullable(recruitApplication));
        Mockito.lenient().when(recruitApplicationRepository.findByRecruitIdAndMatchStatusFetchMember(1L, MatchStatus.DONE))
                .thenReturn(recruitApplications);

        Mockito.lenient().when(memberRepository.getReferenceById(1L)).thenReturn(register);
        Mockito.lenient().when(memberRepository.getReferenceById(2L)).thenReturn(participant);
        Mockito.lenient().when(memberRepository.getReferenceById(3L))
                .thenThrow(new DataIntegrityViolationException(""));

        // MetaData Consumer Mocking
        Arrays.stream(RecruitType.values()).forEach(recruitType -> Mockito.lenient()
                .when(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType.getName()))
                .thenReturn(new MetaData(recruitType)));
    }

    @DisplayName("사용자 리크루트 참여 신청")
    @Test
    void Given_NormalRequest_When_SaveRecruitApplication_Then_Success() {
        PostRecruitApplicationReqDto postRecruitApplicationReqDto = new PostRecruitApplicationReqDto(RecruitType.DESIGN.getName(), List.of("댭변 내용"));

        assertDoesNotThrow(()->{
            recruitApplicationService.saveRecruitApplication(1L, 2L, postRecruitApplicationReqDto);
        });
    }

    @DisplayName("모집하지않는 모집군에 대한 사용자 참여 신청")
    @Test
    void Given_NotExistRecruitTypeInRecruit_When_SaveRecruitApplication_Then_Fail() {
        PostRecruitApplicationReqDto postRecruitApplicationReqDto = new PostRecruitApplicationReqDto(RecruitType.APP.getName(), List.of("댭변 내용"));

        assertThrows(RecruitException.class, ()->{
            recruitApplicationService.saveRecruitApplication(1L, 2L, postRecruitApplicationReqDto);
        });
    }

    @DisplayName("이미 모두 모집된 리크루트글에 대한 사용자 참여 신청")
    @Test
    void Given_AlreadyFullRecruit_When_SaveRecruitApplication_Then_Fail() {
        PostRecruitApplicationReqDto postRecruitApplicationReqDto = new PostRecruitApplicationReqDto(RecruitType.DESIGN.getName(), List.of("댭변 내용"));

        List<RecruitLimitation> limits = List.of(RecruitLimitation.builder()
                .recruit(recruit)
                .limitation(2)
                .currentNumber(2)
                .type(new MetaData(RecruitType.DESIGN))
                .build());
        recruit.setRecruitLimitations(limits);

        assertThrows(RecruitException.class, ()->{
            recruitApplicationService.saveRecruitApplication(1L, 2L, postRecruitApplicationReqDto);
        });
    }

    @DisplayName("등록자 질문에 대한 답변이 없는 사용자 참여 신청")
    @Test
    void Given_EmptyRequestQuestionReply_When_SaveRecruitApplication_Then_Fail() {
        PostRecruitApplicationReqDto postRecruitApplicationReqDto = new PostRecruitApplicationReqDto(RecruitType.DESIGN.getName(), List.of(""));

        assertThrows(RecruitException.class, ()->{
            recruitApplicationService.saveRecruitApplication(1L, 2L, postRecruitApplicationReqDto);
        });
    }

    @DisplayName("등록자 질문과 답변의 개수가 다른 비정상적인 사용자 참여 신청")
    @Test
    void Given_NotSameLengthRecruitQuestionReply_When_SaveRecruitApplication_Then_Fail() {
        PostRecruitApplicationReqDto postRecruitApplicationReqDto = new PostRecruitApplicationReqDto(RecruitType.DESIGN.getName(), List.of("1", "2"));

        assertThrows(RecruitException.class, ()->{
            recruitApplicationService.saveRecruitApplication(1L, 2L, postRecruitApplicationReqDto);
        });
    }

    @DisplayName("등록자 승인 전, 사용자 리크루트 참여 신청 취소")
    @Test
    void Given_NormalRequest_When_CancelRecruitApplication_Then_Success() {
        assertDoesNotThrow(()-> recruitApplicationService.cancelRecruitApplicationByParticipant(1L, 2L, MatchStatus.CANCEL));
    }

    @DisplayName("등록자 승인 이후 사용자 리크루트 참여 신청 취소 실패")
    @Test
    void Given_AfterApproveByRegisterRecruitApplication_When_CancelRecruitApplication_Then_Fail() {
        recruitApplication.changeStatus(MatchStatus.WAITING_APPLICANT);
        assertThrows(RecruitException.class, ()->recruitApplicationService.cancelRecruitApplicationByParticipant(1L, 2L, MatchStatus.CANCEL));
    }

    @DisplayName("등록자 승인 사용자 리크루트 신청 승인")
    @Test
    void Given_NormalRequest_When_ApproveRecruitApplicationByRegister_Then_Success() {
        assertDoesNotThrow(()-> recruitApplicationService.approveRecruitApplicationByRegister(1L, 1L, MatchStatus.WAITING_APPLICANT));
    }

    @DisplayName("이미 모두 모집된 리크루트글에 대한 등록자 승인 사용자 리크루트 신청 승인")
    @Test
    void Given_AlreadyFullRecruitApplication_When_ApproveRecruitApplicationByRegister_Then_Success() {
        List<RecruitLimitation> limits = List.of(RecruitLimitation.builder()
                .recruit(recruit)
                .limitation(2)
                .currentNumber(2)
                .type(new MetaData(RecruitType.DESIGN))
                .build());
        recruit.setRecruitLimitations(limits);
        assertThrows(RecruitException.class, ()-> recruitApplicationService.approveRecruitApplicationByRegister(1L, 1L, MatchStatus.WAITING_APPLICANT));
    }

    @DisplayName("등록자가 아닌 사용자의 사용자 리크루트 신청 승인 실패")
    @Test
    void Given_NotRegister_When_ApproveRecruitApplicationByRegister_Then_Fail() {
        assertThrows(RecruitException.class, ()-> recruitApplicationService.approveRecruitApplicationByRegister(1L, 2L, MatchStatus.WAITING_APPLICANT));
    }

    @DisplayName("취소된 신청에 대한 등록자 승인 실패")
    @Test
    void Given_CancelRecruitApplication_When_ApproveRecruitApplicationByRegister_Then_Fail() {
        recruitApplication.changeStatus(MatchStatus.CANCEL);
        assertThrows(RecruitException.class, ()-> recruitApplicationService.approveRecruitApplicationByRegister(1L, 1L, MatchStatus.WAITING_APPLICANT));
    }

    @DisplayName("사용자 리크루트 신청 최종 참여 승인")
    @Test
    void Given_NormalRequest_When_JoinRecruitApplicationByParticipant_Then_Success() {
        recruitApplication.changeStatus(MatchStatus.WAITING_APPLICANT);
        recruitApplicationService.joinRecruitApplication(1L, 2L, MatchStatus.DONE);
        assertEquals(1, limits.get(0).getCurrentNumber());
    }

    @DisplayName("사용자 리크루트 신청 최종 참여 거절")
    @Test
    void Given_NormalRequest_When_RejectRecruitApplicationByParticipant_Then_Success() {
        recruitApplication.changeStatus(MatchStatus.WAITING_APPLICANT);
        assertDoesNotThrow(
                ()->recruitApplicationService.rejectRecruitApplication(1L, 2L, MatchStatus.REJECT)
        );
    }

    @DisplayName("사용자 리크루트 신청 최종 참여 거절 실패")
    @Test
    void Given_NotValidMatchStatus_When_RejectRecruitApplicationByParticipant_Then_Fail() {
        assertThrows(RecruitException.class,
                ()->recruitApplicationService.rejectRecruitApplication(1L, 2L, MatchStatus.REJECT)
        );
    }

    @DisplayName("등록자 리크루트 신청 거절")
    @Test
    void Given_NormalRequest_When_RejectRecruitApplicationByRegister_Then_Success() {
        assertDoesNotThrow(
                ()->recruitApplicationService.rejectRecruitApplication(1L, 1L, MatchStatus.REJECT)
        );
    }

    @DisplayName("등록자 리크루트 신청 거절 실패")
    @Test
    void Given_NotValidStatus_When_RejectRecruitApplicationByRegister_Then_Success() {
        recruitApplication.changeStatus(MatchStatus.WAITING_APPLICANT);
        assertThrows(RecruitException.class,
                ()->recruitApplicationService.rejectRecruitApplication(1L, 1L, MatchStatus.REJECT)
        );
    }

    @DisplayName("리크루트 참여자 목록 조회")
    @Test
    void Given_RecruitId_When_GetRecruitParticipants_Then_Success() {
        recruitApplicationService.getRecruitParticipants(1L);
        assertDoesNotThrow(()->{
            recruitApplicationService.getRecruitParticipants(1L);
        });
    }
}