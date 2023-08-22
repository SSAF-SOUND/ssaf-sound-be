package com.ssafy.ssafsound.domain.recruitapplication.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitApplicationStatusResDto;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitApplicationsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitParticipantsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;
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
            .data(recruitApplicationService.approveRecruitApplicationByRegister(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.WAITING_APPLICANT))
            .build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/join")
    public EnvelopeResponse<PatchRecruitApplicationStatusResDto> joinRecruitApplication(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PatchRecruitApplicationStatusResDto>builder()
            .data(recruitApplicationService.joinRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.DONE))
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
            .data(recruitApplicationService.cancelRecruitApplicationByParticipant(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.CANCEL))
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
    public EnvelopeResponse<RecruitApplicationElement> getRecruitApplicationByIdAndRegisterId(@PathVariable Long recruitApplicationId, @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<RecruitApplicationElement>builder()
                .data(recruitApplicationService.getRecruitApplicationByIdAndRegisterId(recruitApplicationId, authenticatedMember.getMemberId()))
                .build();
    }
}
