package com.ssafy.ssafsound.domain.recruit.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.recruit.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.service.RecruitService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping("/recruits")
    public EnvelopeResponse<Void> saveRecruit(AuthenticatedMember memberInfo, @Valid @RequestBody PostRecruitReqDto recruitReqDto) {
        recruitService.saveRecruit(memberInfo, recruitReqDto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/recruits/{recruitId}/scrap")
    public EnvelopeResponse<Void> toggleRecruitScrap(@PathVariable Long recruitId, AuthenticatedMember memberInfo) {
        recruitService.toggleRecruitScrap(recruitId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/recruits/{recruitId}/application")
    public EnvelopeResponse<Void> saveRecruitApplication(@PathVariable Long recruitId, AuthenticatedMember memberInfo, @RequestBody PostRecruitApplicationReqDto dto) {
        recruitService.saveRecruitApplication(recruitId, memberInfo.getMemberId(), dto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/approve")
    public EnvelopeResponse<Void> approveRecruitApplicationByRegister(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitService.approveRecruitApplicationByRegister(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.WAITING_APPLICANT);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/join")
    public EnvelopeResponse<Void> joinRecruitApplication(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitService.joinRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.DONE);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/reject")
    public EnvelopeResponse<Void> rejectRecruitApplication(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitService.rejectRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.REJECT);
        return EnvelopeResponse.<Void>builder().build();
    }
}
