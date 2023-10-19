package com.ssafy.ssafsound.domain.term.controller;

import com.ssafy.ssafsound.domain.term.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;
}
