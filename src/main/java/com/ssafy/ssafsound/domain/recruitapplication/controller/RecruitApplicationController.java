package com.ssafy.ssafsound.domain.recruitapplication.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitApplicationsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.GetRecruitParticipantsResDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;
import com.ssafy.ssafsound.domain.recruitapplication.service.RecruitApplicationService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitApplicationController {

    private final RecruitApplicationService recruitApplicationService;

    @PostMapping("/recruits/{recruitId}/application")
    public EnvelopeResponse<Void> saveRecruitApplication(@PathVariable Long recruitId, AuthenticatedMember memberInfo,
                                                         @RequestBody PostRecruitApplicationReqDto postRecruitApplicationReqDto) {
        memberInfo = AuthenticatedMember.builder().memberId(2L).build();
        recruitApplicationService.saveRecruitApplication(recruitId, memberInfo.getMemberId(), postRecruitApplicationReqDto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/approve")
    public EnvelopeResponse<Void> approveRecruitApplicationByRegister(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitApplicationService.approveRecruitApplicationByRegister(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.WAITING_APPLICANT);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/join")
    public EnvelopeResponse<Void> joinRecruitApplication(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitApplicationService.joinRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.DONE);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/reject")
    public EnvelopeResponse<Void> rejectRecruitApplication(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitApplicationService.rejectRecruitApplication(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.REJECT);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-applications/{recruitApplicationId}/cancel")
    public EnvelopeResponse<Void> cancelRecruitApplicationByParticipant(@PathVariable Long recruitApplicationId, AuthenticatedMember memberInfo) {
        recruitApplicationService.cancelRecruitApplicationByParticipant(recruitApplicationId, memberInfo.getMemberId(), MatchStatus.CANCEL);
        return EnvelopeResponse.<Void>builder().build();
    }

    @GetMapping("/recruits/{recruitId}/members")
    public EnvelopeResponse<GetRecruitParticipantsResDto> getRecruitParticipants(@PathVariable Long recruitId) {
        return EnvelopeResponse.<GetRecruitParticipantsResDto>builder()
                .data(recruitApplicationService.getRecruitParticipants(recruitId))
                .build();
    }

    @GetMapping("/recruit-applications")
    public EnvelopeResponse<GetRecruitApplicationsResDto> getRecruitApplications(@RequestParam Long recruitId, AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetRecruitApplicationsResDto>builder()
                .data(recruitApplicationService.getRecruitApplications(recruitId, 1L))
                .build();
    }

    @PostMapping("/recruit-applications/{recruitApplicationId}/like")
    public EnvelopeResponse<Void> toggleRecruitApplicationLike(@PathVariable Long recruitApplicationId, AuthenticatedMember authenticatedMember) {
        recruitApplicationService.toggleRecruitApplicationLike(recruitApplicationId, authenticatedMember.getMemberId());
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @GetMapping("/recruit-applications/{recruitApplicationId}")
    public EnvelopeResponse<RecruitApplicationElement> getRecruitApplicationByIdAndRegisterId(@PathVariable Long recruitApplicationId, AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<RecruitApplicationElement>builder()
                .data(recruitApplicationService.getRecruitApplicationByIdAndRegisterId(recruitApplicationId, 1L))
                .build();
    }
}
