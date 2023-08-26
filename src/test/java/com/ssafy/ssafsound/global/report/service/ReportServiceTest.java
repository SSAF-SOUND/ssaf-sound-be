package com.ssafy.ssafsound.global.report.service;

import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.report.domain.ReportReason;
import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;
import com.ssafy.ssafsound.global.report.exception.ReportErrorInfo;
import com.ssafy.ssafsound.global.report.exception.ReportException;
import com.ssafy.ssafsound.global.report.repository.ReportReasonRepository;
import com.ssafy.ssafsound.global.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MetaDataConsumer metaDataConsumer;
    @Mock
    private ReportReasonRepository reportReasonRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private RecruitRepository recruitRepository;
    @Mock
    private RecruitCommentRepository recruitCommentRepository;

    @InjectMocks
    private ReportService reportService;

    private Member testMember1;

    private Member testMember2;
    private Post testPost1;

    private Post testPost2;

    private Recruit testRecruit;

    private Comment testComment;

    private RecruitComment testRecruitComment;

    private final ReportReason reportReason1 = ReportReason.builder()
            .id(1L)
            .reason("게시판 성격에 부적절함")
            .build();

    private final ReportReason reportReason4 = ReportReason.builder()
            .id(4L)
            .reason("상업적 광고 및 판매")
            .build();

    @BeforeEach
    void setUp() {

        testMember1 = Member.builder()
                .id(1L)
                .build();

        testMember2 = Member.builder()
                .id(2L)
                .build();

        testPost1 = Post.builder()
                .id(1L)
                .member(testMember1)
                .build();

        testPost2 = Post.builder()
                .id(1L)
                .member(testMember2)
                .build();

        testRecruit = Recruit.builder()
                .id(3L)
                .member(testMember2)
                .build();

        testComment = Comment.builder()
                .id(1L)
                .member(testMember2)
                .build();

        testRecruitComment = RecruitComment.builder()
                .id(1L)
                .member(testMember2)
                .build();

        lenient().when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember1));
        lenient().when(memberRepository.findById(2L)).thenReturn(Optional.of(testMember2));

        lenient().when(postRepository.findByIdRegardlessOfDeleted(1L)).thenReturn(Optional.of(testPost1));
        lenient().when(postRepository.findByIdRegardlessOfDeleted(2L)).thenReturn(Optional.of(testPost2));
        lenient().when(recruitRepository.findById(3L)).thenReturn(Optional.of(testRecruit));
        lenient().when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        lenient().when(recruitCommentRepository.findById(1L)).thenReturn(Optional.of(testRecruitComment));

        lenient().when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "post"))
                .thenReturn(new MetaData(SourceType.POST));
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "recruit"))
                .thenReturn(new MetaData(SourceType.RECRUIT));
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "comment"))
                .thenReturn(new MetaData(SourceType.COMMENT));
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "recruit_comment"))
                .thenReturn(new MetaData(SourceType.RECRUIT_COMMENT));
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "chat"))
                .thenReturn(new MetaData(SourceType.CHAT));
    }

    @ParameterizedTest
    @CsvSource({"post, 2, 1", "recruit, 3, 1", "comment, 1, 1", "recruit_comment, 1, 1"})
    @DisplayName("컨텐츠 종류, 컨텐츠 PK, 신고 사유 PK로 신고를 요청한다.")
    void Given_ValidPostSourceTypeAndSourceIdAndReasonId_When_PostReport_Then_Succeed(String sourceType, Long sourceId, Long reasonId) {

        // given
        Long reportMemberId = 1L;

        given(reportReasonRepository.findById(1L)).willReturn(Optional.of(reportReason1));

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when
        reportService.postReport(reportMemberId, postReportReqDto);

        // then
        verify(reportReasonRepository).findById(1L);

    }

    @Test
    @DisplayName("자기 자신의 컨텐츠를 신고 요청할 시에 예외를 던진다.")
    void Given_InvalidSourceType_When_PostReport_Then_ThrowException() {

        // given
        Long reportMemberId = 1L;

        String sourceType = "post";
        Long sourceId = 1L;
        Long reasonId = 1L;

        given(reportReasonRepository.findById(1L)).willReturn(Optional.of(reportReason1));

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when, then
        ReportException exception = assertThrows(ReportException.class,
                () -> reportService.postReport(reportMemberId, postReportReqDto));
        assertEquals(exception.getInfo(), ReportErrorInfo.UNABLE_SELF_REPORT);

        verify(metaDataConsumer).getMetaData(MetaDataType.SOURCE_TYPE.name(), "post");
        verify(reportReasonRepository).findById(1L);
    }

    @Test
    @DisplayName("잘못된 신고 사유 PK로 신고 요청할 시에 예외를 던진다.")
    void Given_InvalidReportReasonId_When_PostReport_Then_ThrowException() {

        // given
        Long reportMemberId = 1L;

        String sourceType = "post";
        Long sourceId = 2L;
        Long reasonId = 999L;

        given(reportReasonRepository.findById(999L)).willReturn(Optional.empty());

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when, then
        ReportException exception = assertThrows(ReportException.class,
                () -> reportService.postReport(reportMemberId, postReportReqDto));
        assertEquals(exception.getInfo(), ReportErrorInfo.INVALID_REPORT_REASON);
    }
}