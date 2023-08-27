package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.global.report.domain.Report;
import com.ssafy.ssafsound.global.report.domain.ReportReason;
import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;

import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.MEMBER_WALTER;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.POST_FIXTURE1;

public class ReportFixture {

    public static final ReportReason REPORT_REASON_FIXTURE1 = ReportReason.builder()
            .id(1L)
            .reason("게시판 성격에 부적절함")
            .build();

    public static final PostReportReqDto POST_REPORT_REQ_DTO = PostReportReqDto.builder()
            .sourceType("RECRUIT")
            .sourceId(1L)
            .reasonId(4L)
            .build();

    public static final Report REPORT_FIXTURE1 = Report.builder()
            .id(111L)
            .sourceType(new MetaData(SourceType.POST))
            .sourceId(POST_FIXTURE1.getId())
            .reportReason(REPORT_REASON_FIXTURE1)
            .reportMemberId(MEMBER_WALTER.getId())
            .reportedMemberId(POST_FIXTURE1.getMember().getId())
            .build();
}
