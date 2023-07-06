package com.ssafy.ssafsound.domain.recruitcomment.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitCommentLike;
import com.ssafy.ssafsound.domain.recruitcomment.dto.GetRecruitCommentsResDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PatchRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentLikeRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecruitCommentServiceTest {

    @Mock
    RecruitCommentRepository recruitCommentRepository;

    @Mock
    RecruitCommentLikeRepository recruitCommentLikeRepository;

    @Mock
    RecruitRepository recruitRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    RecruitCommentService recruitCommentService;

    Member member1 = Member.builder()
            .id(1L)
            .nickname("khs")
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .major(true)
            .majorType(new MajorType(1, "Test"))
            .build();

    Member member2 = Member.builder()
            .id(2L)
            .nickname("kds")
            .ssafyMember(true)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .major(true)
            .majorType(new MajorType(1, "Test"))
            .build();

    Recruit recruit = Recruit.builder()
            .id(1L)
            .member(member1)
            .startDateTime(LocalDate.now().atStartOfDay())
            .endDateTime(LocalDate.now().plusDays(3).atTime(LocalTime.MAX))
            .view(0L)
            .deletedRecruit(false)
            .build();

    RecruitComment commentGroup = RecruitComment.builder()
            .id(1L)
            .member(member1)
            .content("댓글테스트")
            .build();

    RecruitCommentLike recruitCommentLike = RecruitCommentLike.builder()
            .member(member1)
            .recruitComment(commentGroup)
            .build();

    List<RecruitComment> comments = List.of(
            RecruitComment.builder()
                    .id(1L)
                    .content("리크루트 Question1 Fixture")
                    .recruit(recruit)
                    .deletedComment(false)
                    .member(member2)
                    .build(),
            RecruitComment.builder()
                    .id(2L)
                    .content("리크루트 Answer1 Fixture")
                    .recruit(recruit)
                    .deletedComment(false)
                    .member(member1)
                    .build(),
            RecruitComment.builder()
                    .id(3L)
                    .content("리크루트 Question2 Fixture")
                    .recruit(recruit)
                    .deletedComment(true)
                    .member(member2)
                    .build(),
            RecruitComment.builder()
                    .id(4L)
                    .content("리크루트 Answer2 Fixture")
                    .recruit(recruit)
                    .deletedComment(false)
                    .member(member1)
                    .build()
    );

    @BeforeEach
    void setStubAndFixture() {
        commentGroup.setCommentGroup(commentGroup);

        comments.get(0).setCommentGroup(comments.get(0));
        comments.get(1).setCommentGroup(comments.get(0));
        comments.get(2).setCommentGroup(comments.get(2));
        comments.get(3).setCommentGroup(comments.get(2));

        Mockito.lenient().when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member1));
        Mockito.lenient().when(memberRepository.findById(2L)).thenReturn(Optional.ofNullable(member2));
        Mockito.lenient().when(memberRepository.findById(3L)).thenThrow(new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        Mockito.lenient().when(recruitRepository.getReferenceById(1L)).thenReturn(recruit);
        Mockito.lenient().when(recruitRepository.getReferenceById(2L)).thenThrow(new DataIntegrityViolationException(""));

        Mockito.lenient().when(recruitCommentRepository.getReferenceById(1L)).thenReturn(commentGroup);
        Mockito.lenient().when(recruitCommentRepository.findById(1L)).thenReturn(Optional.ofNullable(commentGroup));
        Mockito.lenient().when(recruitCommentRepository.findByRecruitIdFetchJoinMemberAndReplies(1L)).thenReturn(comments);

        Mockito.lenient().when(recruitCommentLikeRepository.findByRecruitCommentIdAndMemberId(1L, 1L)).thenReturn(Optional.ofNullable(recruitCommentLike));
    }

    @DisplayName("유효한 사용자 리크루팅 QNA 질문(댓글) 등록")
    @Test
    void Given_ValidMemberAndDto_When_InsertRecruitComment_Then_Success() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        PostRecruitCommentReqDto recruitCommentReqDto = new PostRecruitCommentReqDto("댓글테스트", -1L);

        PostRecruitCommentResDto response = recruitCommentService.saveRecruitComment(1L, userInfo, recruitCommentReqDto);

        assertAll(
                ()->assertEquals(member1.getNickname(), response.getNickName()),
                ()->assertEquals(member1.getId(), response.getMemberId()),
                // 리크루트 QNA 질문 등록의 경우 페이지네이션을 위해 자기 자신을 CommentGroup으로 가진다.
                ()->assertEquals(commentGroup.getContent(), response.getContent())
        );
    }

    @DisplayName("유효하지 않은 사용자 리크루팅 QNA 질문(댓글) 등록")
    @Test
    void Given_NotValidMemberAndDto_When_InsertRecruitComment_Then_Fail() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(3L)
                .build();

        PostRecruitCommentReqDto recruitCommentReqDto = new PostRecruitCommentReqDto("댓글테스트", -1L);

        assertThrows(ResourceNotFoundException.class, ()-> recruitCommentService.saveRecruitComment(1L, userInfo, recruitCommentReqDto));
    }

    @DisplayName("존재하지 않은 리크루팅에 대한 QNA 질문(댓글) 등록")
    @Test
    void Given_NotValidRecruitAndDto_When_InsertRecruitComment_Then_Fail() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        PostRecruitCommentReqDto recruitCommentReqDto = new PostRecruitCommentReqDto("댓글테스트", -1L);

        assertThrows(DataIntegrityViolationException.class, ()-> recruitCommentService.saveRecruitComment(2L, userInfo, recruitCommentReqDto));
    }


    @DisplayName("사용자 리크루팅 QNA 답변(대댓글) 등록")
    @Test
    void Given_ValidMemberAndDto_When_InsertAnswerOtherRecruitComment_Then_Success() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        PostRecruitCommentReqDto recruitCommentReqDto = new PostRecruitCommentReqDto("댓글테스트", 1L);

        PostRecruitCommentResDto response = recruitCommentService.saveRecruitComment(1L, userInfo, recruitCommentReqDto);

        // 리크루팅 QNA 답변(대댓글)은 질문(댓글)의 PK를 CommentGroup으로 가진다.
        assertEquals(commentGroup.getId(), response.getCommentGroup());
    }

    @DisplayName("유효한 사용자 리크루팅 댓글 삭제")
    @Test
    void Given_ValidMemberAndDto_When_DeleteRecruitComment_Then_Success() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        assertDoesNotThrow(()->recruitCommentService.deleteRecruitComment(1L, userInfo));
    }

    @DisplayName("유효하지 않은 사용자 리크루팅 댓글 삭제")
    @Test
    void Given_NotValidMemberAndDto_When_DeleteRecruitComment_Then_Fail() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(2L)
                .build();

        assertThrows(RecruitException.class, ()->recruitCommentService.deleteRecruitComment(1L, userInfo));
    }

    @DisplayName("유효한 사용자 리크루팅 댓글 업데이트")
    @Test
    void Given_ValidMemberAndDto_When_UpdateRecruitComment_Then_Success() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(1L)
                .build();

        PatchRecruitCommentReqDto dto = new PatchRecruitCommentReqDto("업데이트");

        RecruitComment afterUpdateRecruitComment = recruitCommentService.updateRecruitComment(1L, userInfo, dto);

        assertEquals("업데이트", afterUpdateRecruitComment.getContent());
    }

    @DisplayName("유효하지 않은 사용자 리크루팅 댓글 업데이트")
    @Test
    void Given_NotValidMemberAndDto_When_UpdateRecruitComment_Then_Success() {
        AuthenticatedMember userInfo = AuthenticatedMember.builder()
                .memberId(2L)
                .build();

        PatchRecruitCommentReqDto dto = new PatchRecruitCommentReqDto("자신이등록하지않은글");

        assertThrows(RecruitException.class, ()->recruitCommentService.updateRecruitComment(1L, userInfo, dto));
    }

    @DisplayName("리크루트 QNA 좋아요 등록 테스트")
    @Test
    void Given_MemberIdAndRecruitCommentId_When_TryToggleRecruitCommentLike_Then_InsertRecruitCommentLike() {
        Long recruitCommentId = 2L, memberId = 1L;
        assertFalse(recruitCommentService.toggleRecruitCommentLike(recruitCommentId, memberId));
    }

    @DisplayName("리크루트 QNA 좋아요 등록취소 테스트")
    @Test
    void Given_MemberIdAndRecruitCommentId_When_TryToggleRecruitCommentLike_Then_DeleteRecruitLike() {
        Long recruitCommentId = 1L, memberId = 1L;
        assertTrue(recruitCommentService.toggleRecruitCommentLike(recruitCommentId, memberId));
    }

    @DisplayName("리크루트 QNA 리스트 조회")
    @Test
    void Given_RecruitComments_When_TryGetRecruitComments_Then_Success() {
        GetRecruitCommentsResDto recruitComments = recruitCommentService.getRecruitComments(1L);

        assertAll(
                ()->assertEquals(2, recruitComments.getRecruitComments().size()),
                ()-> recruitComments.getRecruitComments()
                        .forEach(recruitCommentElement -> assertEquals(1, recruitCommentElement.getChildren().size()))
        );
    }
}