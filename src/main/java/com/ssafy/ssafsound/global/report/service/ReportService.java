package com.ssafy.ssafsound.global.report.service;

import com.ssafy.ssafsound.domain.chat.repository.ChatRepository;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.report.domain.Report;
import com.ssafy.ssafsound.global.report.domain.ReportReason;
import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;
import com.ssafy.ssafsound.global.report.dto.PostReportResDto;
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
    private final ChatRepository chatRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitCommentRepository recruitCommentRepository;

    @Transactional
    public PostReportResDto postReport(Long memberId, PostReportReqDto postReportReqDto) {

        Member reportMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        MetaData sourceType = metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), postReportReqDto.getSourceType());

        ReportReason reason = reportReasonRepository.findById(postReportReqDto.getReasonId()).orElseThrow();

        Report report = Report.builder()
                .sourceType(sourceType)
                .sourceId(postReportReqDto.getSourceId())
                .reportMemberId(reportMember.getId())
                .reportedMemberId(getMemberIdFromSourceContent(sourceType, postReportReqDto.getSourceId()))
                .reportReason(reason)
                .build();

        return null;
    }

    private Long getMemberIdFromSourceContent(MetaData sourceType, Long sourceId) {

        if (sourceType.getName().equals(SourceType.POST.getName())) {

            return postRepository.findById(sourceId).get().getId();
        }

        if (sourceType.getName().equals(SourceType.COMMENT.getName())) {

            return commentRepository.findById(sourceId).get().getId();
        }

        if (sourceType.getName().equals(SourceType.CHAT.getName())) {

            return chatRepository.findById(sourceId).get().getId();
        }

        if (sourceType.getName().equals(SourceType.RECRUIT.getName())) {

            return recruitRepository.findById(sourceId).get().getId();
        }

        if (sourceType.getName().equals(SourceType.RECRUIT_COMMENT.getName())) {

            return recruitCommentRepository.findById(sourceId).get().getId();
        }

        throw new RuntimeException();
    }
}
