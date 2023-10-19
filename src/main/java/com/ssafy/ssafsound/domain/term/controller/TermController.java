package com.ssafy.ssafsound.domain.term.controller;

import com.ssafy.ssafsound.domain.term.dto.GetTermsResDto;
import com.ssafy.ssafsound.domain.term.service.TermService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping
    public EnvelopeResponse<GetTermsResDto> getTerms() {
        return EnvelopeResponse.<GetTermsResDto>builder()
                .data(termService.getTerms())
                .build();
    }
}
