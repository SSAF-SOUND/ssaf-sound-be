package com.ssafy.ssafsound.global.report.service;

import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import com.ssafy.ssafsound.domain.chat.exception.ChatErrorInfo;
import com.ssafy.ssafsound.domain.chat.exception.ChatException;
import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.comment.exception.CommentErrorInfo;
import com.ssafy.ssafsound.domain.comment.exception.CommentException;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.report.domain.Report;
import com.ssafy.ssafsound.global.report.domain.ReportReason;
import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;
import com.ssafy.ssafsound.global.report.exception.ReportErrorInfo;
import com.ssafy.ssafsound.global.report.exception.ReportException;
import com.ssafy.ssafsound.global.report.repository.ReportReasonRepository;
import com.ssafy.ssafsound.global.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;
    private final ReportReasonRepository reportReasonRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitCommentRepository recruitCommentRepository;

    @Transactional
    public void postReport(Long memberId, PostReportReqDto postReportReqDto) {

        Member reportMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        System.out.println(postReportReqDto.getSourceType());
        System.out.println(postReportReqDto.getReasonId());

        MetaData sourceType = metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(),
            postReportReqDto.getSourceType());

        ReportReason reason = reportReasonRepository.findById(postReportReqDto.getReasonId())
            .orElseThrow(() -> new ReportException(ReportErrorInfo.INVALID_REPORT_REASON));

        Report report = Report.builder()
                .sourceType(sourceType)
                .sourceId(postReportReqDto.getSourceId())
                .reportMemberId(reportMember.getId())
                .reportedMemberId(getMemberIdFromSourceContent(sourceType, postReportReqDto.getSourceId()))
                .reportReason(reason)
                .build();

        reportRepository.save(report);
    }

    private Long getMemberIdFromSourceContent(MetaData sourceType, Long sourceId) {

        if (sourceType.getName().equals(SourceType.POST.getName())) {

            return postRepository.findById(sourceId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST))
                .getMember().getId();
        }

        if (sourceType.getName().equals(SourceType.COMMENT.getName())) {

            return commentRepository.findById(sourceId)
                .orElseThrow(() -> new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT))
                .getMember().getId();
        }

        if (sourceType.getName().equals(SourceType.CHAT.getName())) {

            ChatRoom chatRoom = chatRoomRepository.findById(sourceId)
                .orElseThrow(() -> new ChatException(ChatErrorInfo.INVALID_CHAT_ROOM_ID));

            // 채팅 기능 추가 후 수정 필요
            Long reportedMemberId = null;
//            Long reportedMemberId = chatRoom.getTalkers().getMember().getId();

            return reportedMemberId;
        }

        if (sourceType.getName().equals(SourceType.RECRUIT.getName())) {

            return recruitRepository.findById(sourceId)
                .orElseThrow(()->new RecruitException(RecruitErrorInfo.NOT_FOUND_RECRUIT))
                .getMember().getId();
        }

        if (sourceType.getName().equals(SourceType.RECRUIT_COMMENT.getName())) {

            return recruitCommentRepository.findById(sourceId)
                .orElseThrow(()->new RecruitException(RecruitErrorInfo.NOT_FOUND_RECRUIT_COMMENT))
                .getMember().getId();
        }

        throw new RuntimeException();
    }
}
