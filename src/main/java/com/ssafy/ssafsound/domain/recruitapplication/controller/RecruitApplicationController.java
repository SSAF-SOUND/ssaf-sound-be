package com.ssafy.ssafsound.domain.recruitapplication.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.recruit.dto.GetRejectedRecruitApplicationsResDto;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitApplicationStatusResDto;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.dto.*;
import com.ssafy.ssafsound.domain.recruitapplication.service.RecruitApplicationService;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitApplicationLikeResDto;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitApplicationController {

    private final RecruitApplicationService recruitApplicationService;

    @PostMapping("/recruits/{recruitId}/application")
    public EnvelopeResponse<PatchRecruitApplicationStatusResDto> saveRecruitApplication(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo,
                                                         @RequestBody PostRecruitApplicationReqDto postRecruitApplicationReqDto) {
        return EnvelopeResponse.<PatchRecruitApplicationStatusResDto>builder()
                .data(recruitApplicationService.saveRecruitApplication(recruitId, memberInfo.getMemberId(), postRecruitApplicationReqDto))
                .build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/approve")
    public EnvelopeResponse<PatchRecruitApplicationStatusResDto> approveRecruitApplicationByRegister(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PatchRecruitApplicationStatusResDto>builder()
            .data(recruitApplicationService.approveRecruitApplicationByRegister(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.DONE))
            .build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/reject")
    public EnvelopeResponse<PatchRecruitApplicationStatusResDto> rejectRecruitApplication(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PatchRecruitApplicationStatusResDto>builder()
            .data(recruitApplicationService.rejectRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.REJECT))
            .build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/cancel")
    public EnvelopeResponse<PatchRecruitApplicationStatusResDto> cancelRecruitApplicationByParticipant(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PatchRecruitApplicationStatusResDto>builder()
            .data(recruitApplicationService.cancelRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.CANCEL))
            .build();
    }

    @GetMapping("/recruits/{recruitId}/members")
    public EnvelopeResponse<GetRecruitParticipantsResDto> getRecruitParticipants(@PathVariable Long recruitId) {
        return EnvelopeResponse.<GetRecruitParticipantsResDto>builder()
                .data(recruitApplicationService.getRecruitParticipants(recruitId))
                .build();
    }

    @GetMapping("/recruit-applications")
    public EnvelopeResponse<GetRecruitApplicationsResDto> getRecruitApplications(@RequestParam Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitApplicationsResDto>builder()
                .data(recruitApplicationService.getRecruitApplications(recruitId, memberInfo.getMemberId()))
                .build();
    }

    @PostMapping("/recruit-applications/{recruitApplicationId}/like")
    public EnvelopeResponse<PostRecruitApplicationLikeResDto> toggleRecruitApplicationLike(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PostRecruitApplicationLikeResDto>builder()
            .data(recruitApplicationService.toggleRecruitApplicationLike(recruitApplicationId, memberInfo.getMemberId()))
            .build();
    }

    @GetMapping("/recruit-applications/{recruitApplicationId}")
    public EnvelopeResponse<GetRecruitApplicationDetailResDto> getRecruitApplicationByIdAndRegisterId(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetRecruitApplicationDetailResDto>builder()
                .data(recruitApplicationService.getRecruitApplicationByIdAndRegisterId(recruitApplicationId, authenticatedMember.getMemberId()))
                .build();
    }

    @GetMapping("/recruit-applications/rejected")
    public EnvelopeResponse<GetRejectedRecruitApplicationsResDto> getRejectedRecruitApplicationByRecruitId(Long recruitId, @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetRejectedRecruitApplicationsResDto>builder()
                .data(recruitApplicationService.getRejectedRecruitApplicationByRecruitId(recruitId, authenticatedMember.getMemberId()))
                .build();
    }

    @GetMapping("/recruit-applications/mine")
    public EnvelopeResponse<GetRecruitApplicationDetailResDto> getRecentPendingRecruitApplicationByRecruitId(Long recruitId, @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetRecruitApplicationDetailResDto>builder()
                .data(recruitApplicationService.getRecentPendingRecruitApplicationByRecruitId(recruitId, authenticatedMember.getMemberId()))
                .build();
    }
}
