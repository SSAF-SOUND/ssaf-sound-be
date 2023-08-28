package com.ssafy.ssafsound.global.report.service;

import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
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

import static com.ssafy.ssafsound.global.util.fixture.CommentFixture.COMMENT_FIXTURE_1;
import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.MEMBER_SHERYL;
import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.MEMBER_WALTER;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.POST_FIXTURE1;
import static com.ssafy.ssafsound.global.util.fixture.RecruitCommentFixture.RECRUIT_COMMENT_1;
import static com.ssafy.ssafsound.global.util.fixture.RecruitFixture.RECRUIT_1;
import static com.ssafy.ssafsound.global.util.fixture.ReportFixture.REPORT_FIXTURE1;
import static com.ssafy.ssafsound.global.util.fixture.ReportFixture.REPORT_REASON_FIXTURE1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    private final MetaData sourceTypePost = new MetaData(SourceType.POST);
    private final MetaData sourceTypeRecruit = new MetaData(SourceType.RECRUIT);
    private final MetaData sourceTypeRecruitComment = new MetaData(SourceType.RECRUIT_COMMENT);
    private final MetaData sourceTypeComment = new MetaData(SourceType.COMMENT);
    private final MetaData sourceTypeChat = new MetaData(SourceType.CHAT);

    @BeforeEach
    void setUp() {

        lenient().when(memberRepository.findById(POST_FIXTURE1.getMember().getId()))
                .thenReturn(Optional.of(POST_FIXTURE1.getMember()));
        lenient().when(memberRepository.findById(RECRUIT_1.getMember().getId()))
                .thenReturn(Optional.of(RECRUIT_1.getMember()));
        lenient().when(memberRepository.findById(COMMENT_FIXTURE_1.getMember().getId()))
                .thenReturn(Optional.of(COMMENT_FIXTURE_1.getMember()));
        lenient().when(memberRepository.findById(RECRUIT_COMMENT_1.getMember().getId()))
                .thenReturn(Optional.of(RECRUIT_COMMENT_1.getMember()));

        lenient().when(postRepository.findByIdRegardlessOfDeleted(POST_FIXTURE1.getId()))
                .thenReturn(Optional.of(POST_FIXTURE1));
        lenient().when(recruitRepository.findById(RECRUIT_1.getId())).thenReturn(Optional.of(RECRUIT_1));
        lenient().when(commentRepository.findById(COMMENT_FIXTURE_1.getId())).thenReturn(Optional.of(COMMENT_FIXTURE_1));
        lenient().when(recruitCommentRepository.findById(RECRUIT_COMMENT_1.getId()))
                .thenReturn(Optional.of(RECRUIT_COMMENT_1));

        lenient().when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "post"))
                .thenReturn(sourceTypePost);
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "recruit"))
                .thenReturn(sourceTypeRecruit);
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "comment"))
                .thenReturn(sourceTypeComment);
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "recruit_comment"))
                .thenReturn(sourceTypeRecruitComment);
        lenient().when(metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), "chat"))
                .thenReturn(sourceTypeChat);
    }

    @ParameterizedTest
    @CsvSource({"post, 1, 1", "recruit, 1, 1", "comment, 1, 1", "recruit_comment, 1, 1"})
    @DisplayName("컨텐츠 종류, 컨텐츠 PK, 신고 사유 PK로 신고를 요청한다.")
    void Given_ValidPostSourceTypeAndSourceIdAndReasonId_When_PostReport_Then_Succeed(String sourceType, Long sourceId, Long reasonId) {

        // given
        Long reportMemberId = MEMBER_SHERYL.getId();

        given(memberRepository.findById(MEMBER_SHERYL.getId())).willReturn(Optional.of(MEMBER_SHERYL));
        given(reportReasonRepository.findById(REPORT_REASON_FIXTURE1.getId()))
                .willReturn(Optional.of(REPORT_REASON_FIXTURE1));
        given(reportRepository.existsBySourceTypeAndSourceIdAndReportMemberId(any(), any(), any()))
                .willReturn(Boolean.FALSE);

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when
        reportService.postReport(reportMemberId, postReportReqDto);

        // then
        verify(reportReasonRepository).findById(REPORT_REASON_FIXTURE1.getId());

    }

    @Test
    @DisplayName("자기 자신의 컨텐츠를 신고 요청할 시에 예외를 던진다.")
    void Given_InvalidSourceType_When_PostReport_Then_ThrowException() {

        // given
        Long reportMemberId = POST_FIXTURE1.getMember().getId();

        String sourceType = sourceTypePost.getName();
        Long sourceId = POST_FIXTURE1.getId();
        Long reasonId = REPORT_REASON_FIXTURE1.getId();

        given(memberRepository.findById(reportMemberId)).willReturn(Optional.of(POST_FIXTURE1.getMember()));
        given(reportReasonRepository.findById(REPORT_REASON_FIXTURE1.getId()))
                .willReturn(Optional.of(REPORT_REASON_FIXTURE1));
        given(reportRepository.existsBySourceTypeAndSourceIdAndReportMemberId(sourceTypePost, sourceId, reportMemberId))
                .willReturn(Boolean.FALSE);

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when, then
        ReportException exception = assertThrows(ReportException.class,
                () -> reportService.postReport(reportMemberId, postReportReqDto));
        assertEquals(exception.getInfo(), ReportErrorInfo.UNABLE_SELF_REPORT);

        verify(memberRepository).findById(reportMemberId);
        verify(metaDataConsumer).getMetaData(MetaDataType.SOURCE_TYPE.name(), sourceTypePost.getName());
        verify(reportReasonRepository).findById(REPORT_REASON_FIXTURE1.getId());
        verify(reportRepository).existsBySourceTypeAndSourceIdAndReportMemberId(sourceTypePost, sourceId, reportMemberId);
    }

    @Test
    @DisplayName("잘못된 신고 사유 PK로 신고 요청할 시에 예외를 던진다.")
    void Given_InvalidReportReasonId_When_PostReport_Then_ThrowException() {

        // given
        Long reportMemberId = MEMBER_SHERYL.getId();

        String sourceType = sourceTypePost.getName();
        Long sourceId = POST_FIXTURE1.getId();
        Long reasonId = 999L;

        given(memberRepository.findById(MEMBER_SHERYL.getId())).willReturn(Optional.of(MEMBER_SHERYL));
        given(reportReasonRepository.findById(reasonId)).willReturn(Optional.empty());

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

    @Test
    @DisplayName("이미 신고한 컨텐츠로 신고 요청할 시에 예외를 던진다.")
    void Given_DuplicatedReport_When_PostReport_Then_ThrowException() {

        // given
        Long reportMemberId = MEMBER_WALTER.getId();

        String sourceType = sourceTypePost.getName();
        Long sourceId = REPORT_FIXTURE1.getSourceId();
        Long reasonId = REPORT_REASON_FIXTURE1.getId();

        given(memberRepository.findById(MEMBER_WALTER.getId())).willReturn(Optional.of(MEMBER_WALTER));
        given(reportRepository.existsBySourceTypeAndSourceIdAndReportMemberId(sourceTypePost, sourceId, reportMemberId))
                .willReturn(Boolean.TRUE);

        PostReportReqDto postReportReqDto = PostReportReqDto.builder()
                .sourceType(sourceType)
                .sourceId(sourceId)
                .reasonId(reasonId)
                .build();

        // when, then
        ReportException exception = assertThrows(ReportException.class,
                () -> reportService.postReport(reportMemberId, postReportReqDto));
        assertEquals(exception.getInfo(), ReportErrorInfo.DUPLICATE_REPORT);
    }
}