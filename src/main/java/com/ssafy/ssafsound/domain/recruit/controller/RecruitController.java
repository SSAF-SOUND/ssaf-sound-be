package com.ssafy.ssafsound.domain.recruit.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.service.RecruitService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/recruits")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping("")
    public EnvelopeResponse<Void> saveRecruit(AuthenticatedMember userInfo, @Valid @RequestBody PostRecruitReqDto recruitReqDto) {
        recruitService.saveRecruit(userInfo, recruitReqDto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/recruits/{recruitId}/scrap")
    public EnvelopeResponse<Void> toggleRecruitScrap(@PathVariable Long recruitId, AuthenticatedMember userInfo) {
        recruitService.toggleRecruitScrap(recruitId, userInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }
}
