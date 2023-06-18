package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lunch")
@RequiredArgsConstructor
public class LunchController {

    private final LunchService lunchService;

    @GetMapping("/{lunchId}")
    public EnvelopeResponse<GetLunchResDto> getLunchByLunchId(@PathVariable @NumberFormat Long lunchId){

        return EnvelopeResponse.<GetLunchResDto>builder()
                .data(lunchService.findLunchDetail(lunchId))
                .build();
    }

    @GetMapping
    public EnvelopeResponse<GetLunchListResDto> getLunchesByCampusAndDate(@Valid GetLunchListReqDto getLunchListReqDto){

        return EnvelopeResponse.<GetLunchListResDto>builder()
                .data(lunchService.findLunches(getLunchListReqDto))
                .build();
    }

    @PostMapping("/poll/{lunchId}")
    public EnvelopeResponse<PostLunchPollResDto> postLunchPoll(AuthenticatedMember user, @PathVariable @NumberFormat Long lunchId){

        return EnvelopeResponse.<PostLunchPollResDto>builder()
                .data(lunchService.saveLunchPoll(user.getMemberId(), lunchId))
                .build();
    }
}
