package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.meta.domain.Skill;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Category;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private RecruitApplicationRepository recruitApplicationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MetaDataConsumer metaDataConsumer;

    @InjectMocks
    private RecruitService recruitService;

    private Member member = Member.builder().id(1L).build();

    private final PostRecruitReqDto postRecruitReqDto = new PostRecruitReqDto(
            Category.STUDY.name(), LocalDate.now(),  "스터디/프로젝트 모집 제목", "컨텐츠",
            RecruitType.DESIGN.getName(), Arrays.stream(Skill.values()).map(Skill::getName).collect(Collectors.toList()),
            Collections.singletonList("프로젝트/스터디 등록자가 참여자에게 묻고 싶은 자유 질문"),
            Arrays.stream(RecruitType.values()).map(recruitType-> new RecruitLimitElement(recruitType.getName(), 2)).collect(Collectors.toList()));

    @BeforeEach
    void setStub() {
        // Repository Mocking
        Mockito.lenient().when(memberRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(member));
        Mockito.lenient().when(memberRepository.findById(2L)).thenThrow(new RuntimeException());

        // MetaData Consumer Mocking
        Arrays.stream(RecruitType.values()).forEach(recruitType -> Mockito.lenient()
                .when(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType.getName()))
                .thenReturn(new MetaData(recruitType)));
    }

    @DisplayName("토큰과 정상 리크루트글 등록 요청이 넘어온 경우 리크루트 글 등록 성공")
    @Test
    void Given_AuthenticatedMemberAndPostRecruitReqDto_When_SaveRecruitService_Then_Success() {
        AuthenticatedMember existUser = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        Recruit recruit = recruitService.saveRecruit(existUser, postRecruitReqDto);

        assertAll(
                ()-> assertEquals(member, recruit.getMember()),
                ()-> {
                    RecruitApplication registerApplication = recruit.getApplications().get(0);
                    assertNotNull(registerApplication);
                    assertEquals( 1, recruit.getApplications().size());
                    assertEquals(RecruitType.DESIGN.getName(), registerApplication.getType().getName());
                    assertEquals(1, registerApplication.getMember().getId());
                },
                ()-> {
                    List<RecruitLimitation> recruitLimitations = recruit.getLimitations();
                    assertNotNull(recruitLimitations);
                    assertEquals(RecruitType.values().length, recruitLimitations.size());

                    for(RecruitLimitation recruitLimitation: recruitLimitations) {
                        assertEquals(2, recruitLimitation.getLimitation());
                        if(recruitLimitation.getType().getName().equals(RecruitType.DESIGN.getName())) {
                            assertEquals(1, recruitLimitation.getCurrentNumber());
                        } else {
                            assertEquals(0, recruitLimitation.getCurrentNumber());
                        }
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

        assertThrows(RuntimeException.class, ()-> recruitService.saveRecruit(notExistMember, postRecruitReqDto));
    }
}