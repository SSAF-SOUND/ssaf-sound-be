package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.lunch.service.LunchPollService;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lunch")
@RequiredArgsConstructor
public class LunchController {

    private final LunchService lunchService;
    private final LunchPollService lunchPollService;

    @GetMapping
    public EnvelopeResponse<GetLunchListResDto> getLunchesByCampusAndDate(@Authentication AuthenticatedMember member, @Valid GetLunchListReqDto getLunchListReqDto) {

        return EnvelopeResponse.<GetLunchListResDto>builder()
                .data(lunchService.findLunches(member.getMemberId(), getLunchListReqDto))
                .build();
    }

    @PostMapping("/poll/{lunchId}")
    public EnvelopeResponse<PostLunchPollResDto> postLunchPoll(@Authentication AuthenticatedMember user, @PathVariable @NumberFormat Long lunchId) {

        return EnvelopeResponse.<PostLunchPollResDto>builder()
                .data(lunchPollService.saveLunchPoll(user.getMemberId(), lunchId))
                .build();
    }

    @PostMapping("/poll/revert/{lunchId}")
    public EnvelopeResponse<PostLunchPollResDto> revertLunchPoll(@Authentication AuthenticatedMember user, @PathVariable @NumberFormat Long lunchId) {

        return EnvelopeResponse.<PostLunchPollResDto>builder()
                .data(lunchPollService.deleteLunchPoll(user.getMemberId(), lunchId))
                .build();
    }

}
