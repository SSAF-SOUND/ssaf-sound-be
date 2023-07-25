package com.ssafy.ssafsound.domain.member.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public EnvelopeResponse<GetMemberResDto> getMemberInformation(
            @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetMemberResDto>builder()
                .data(memberService.getMemberInformation(authenticatedMember.getMemberId()))
                .build();
    }

    @GetMapping("/{memberId}/portfolio")
    public EnvelopeResponse<GetMemberPortfolioResDto> getMemberPortfolioById(@PathVariable Long memberId) {
        return EnvelopeResponse.<GetMemberPortfolioResDto>builder()
                .data(memberService.getMemberPortfolioById(memberId))
                .build();
    }

    @GetMapping("/{memberId}/default-information")
    public EnvelopeResponse<GetMemberDefaultInfoResDto> getMemberDefaultInfoByMemberId(@PathVariable Long memberId) {
        return EnvelopeResponse.<GetMemberDefaultInfoResDto>builder()
                .data(memberService.getMemberDefaultInfoByMemberId(memberId))
                .build();
    }

    @GetMapping("/public-profile")
    public EnvelopeResponse<GetMemberPublicProfileResDto> getMemberProfilePublic(
            @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetMemberPublicProfileResDto>builder()
                .data(memberService.getMemberPublicProfileByMemberId(authenticatedMember.getMemberId()))
                .build();
    }

    @PutMapping
    public EnvelopeResponse<GetMemberResDto> registerMemberInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PostMemberInfoReqDto postMemberInfoReqDto) {
        return EnvelopeResponse.<GetMemberResDto>builder()
                .data(memberService.registerMemberInformation(authenticatedMember, postMemberInfoReqDto))
                .build();
    }

    @PostMapping("/nickname")
    public EnvelopeResponse<PostNicknameResDto> checkNicknamePossible(
            @Valid @RequestBody PostNicknameReqDto postNicknameReqDto) {
        return EnvelopeResponse.<PostNicknameResDto>builder()
                .data(memberService.checkNicknamePossible(postNicknameReqDto))
                .build();
    }

    @PostMapping("/ssafy-certification")
    public EnvelopeResponse<PostCertificationInfoResDto> certifySSAFYInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PostCertificationInfoReqDto postCertificationInfoReqDto) {
        return EnvelopeResponse.<PostCertificationInfoResDto>builder()
                .data(memberService.certifySSAFYInformation(
                        authenticatedMember.getMemberId(),
                        postCertificationInfoReqDto))
                .build();
    }

    @PutMapping("/portfolio")
    public EnvelopeResponse registerMemberPortfolio(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PutMemberProfileReqDto putMemberProfileReqDto) {
        memberService.registerMemberPortfolio(
                authenticatedMember.getMemberId(),
                putMemberProfileReqDto);
        return EnvelopeResponse.builder()
                .build();
    }

    @PatchMapping("/default-information")
    public EnvelopeResponse patchMemberDefaultInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberDefaultInfoReqDto patchMemberDefaultInfoReqDto) {
        memberService.patchMemberDefaultInfo(authenticatedMember.getMemberId(), patchMemberDefaultInfoReqDto);
        return EnvelopeResponse.builder()
                .build();
    }

    @PatchMapping("/public-profile")
    public EnvelopeResponse patchMemberPublicProfile(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto) {

        memberService.patchMemberPublicProfile(authenticatedMember.getMemberId(), patchMemberPublicProfileReqDto);
        return EnvelopeResponse.builder()
                .build();
    }

    @PatchMapping("/nickname")
    public EnvelopeResponse changeMemberNickname(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberNicknameReqDto patchMemberNicknameReqDto) {

        memberService.changeMemberNickname(authenticatedMember.getMemberId(), patchMemberNicknameReqDto);
        return EnvelopeResponse.builder()
                .build();
    }
}
