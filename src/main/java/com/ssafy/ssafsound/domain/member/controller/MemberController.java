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

    /**
     *  나의 포트폴리오 가져오기
     * @author : YongsHub
     * @param : memberId
     * 나의 포트폴리오 조회는 공개 여부와 상관 없이 가져올 수 있어야 합니다.
     */
    @GetMapping("/portfolio")
    public EnvelopeResponse<GetMemberPortfolioResDto> getMyPortfolio(
            @Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetMemberPortfolioResDto>builder()
                .data(memberService.getMyPortfolio(authenticatedMember.getMemberId()))
                .build();
    }

    /**
     *  멤버 포트폴리오 가져오기
     * @author : YongsHub
     * @param : memberId
     * 멤버 포트폴리오 조회는 공개 여부에 따라 조회가 될 수도 있고 조회가 안될수 있습니다.
     */
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

    @GetMapping("/{memberId}/public-profile")
    public EnvelopeResponse<GetMemberPublicProfileResDto> getOtherMemberProfilePublic(@PathVariable Long memberId) {
        return EnvelopeResponse.<GetMemberPublicProfileResDto>builder()
                .data(memberService.getMemberPublicProfileByMemberId(memberId))
                .build();
    }

    @PutMapping
    public EnvelopeResponse<GetMemberResDto> registerMemberInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PostMemberInfoReqDto postMemberInfoReqDto) {
        return EnvelopeResponse.<GetMemberResDto>builder()
                .data(memberService.registerMemberInformation(authenticatedMember.getMemberId(), postMemberInfoReqDto))
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
    public EnvelopeResponse<Void> registerMemberPortfolio(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PutMemberPortfolioReqDto putMemberPortfolioReqDto) {
        memberService.registerMemberPortfolio(
                authenticatedMember.getMemberId(),
                putMemberPortfolioReqDto);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PatchMapping("/default-information")
    public EnvelopeResponse<Void> patchMemberDefaultInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberDefaultInfoReqDto patchMemberDefaultInfoReqDto) {
        memberService.patchMemberDefaultInfo(authenticatedMember.getMemberId(), patchMemberDefaultInfoReqDto);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PatchMapping("/public-profile")
    public EnvelopeResponse<Void> patchMemberPublicProfile(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto) {

        memberService.patchMemberPublicProfile(authenticatedMember.getMemberId(), patchMemberPublicProfileReqDto);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PatchMapping("/major")
    public EnvelopeResponse<Void> patchMemberIsMajor(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberMajorReqDto patchMemberMajorReqDto) {

        memberService.changeMemberMajor(authenticatedMember.getMemberId(), patchMemberMajorReqDto);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PatchMapping("/major-track")
    public EnvelopeResponse<Void> patchMemberMajorTrack(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberMajorTrackReqDto patchMemberMajorTrackReqDto) {

        memberService.changeMemberMajorTrack(
                authenticatedMember.getMemberId(),
                patchMemberMajorTrackReqDto.getMajorTrack());
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PatchMapping("/nickname")
    public EnvelopeResponse<Void> changeMemberNickname(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberNicknameReqDto patchMemberNicknameReqDto) {

        memberService.changeMemberNickname(authenticatedMember.getMemberId(), patchMemberNicknameReqDto);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @DeleteMapping
    public EnvelopeResponse<Void> leaveMember(@Authentication AuthenticatedMember authenticatedMember) {
        memberService.leaveMember(authenticatedMember.getMemberId());

        return EnvelopeResponse.<Void>builder()
                .build();
    }
}
