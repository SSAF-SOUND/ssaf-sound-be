package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;

public class ReportFixture {

    public static final PostReportReqDto POST_REPORT_REQ_DTO = PostReportReqDto.builder()
            .sourceType("RECRUIT")
            .sourceId(1L)
            .reasonId(4L)
            .build();
}
