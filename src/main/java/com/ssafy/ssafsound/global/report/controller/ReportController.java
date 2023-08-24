package com.ssafy.ssafsound.global.report.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import com.ssafy.ssafsound.global.report.dto.PostReportReqDto;
import com.ssafy.ssafsound.global.report.dto.PostReportResDto;
import com.ssafy.ssafsound.global.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public EnvelopeResponse<PostReportResDto> postReport(@Authentication AuthenticatedMember member, PostReportReqDto postReportReqDto) {

        return EnvelopeResponse.<PostReportResDto>builder()
                .data(reportService.postReport(member.getMemberId(), postReportReqDto))
                .build();
    }

}
