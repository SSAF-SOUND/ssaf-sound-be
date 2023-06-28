package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public AuthenticatedMember createMemberByOauthIdentifier(PostMemberReqDto postMemberReqDto) {
        Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier());
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            if (isInvalidOauthLogin(member, postMemberReqDto)) throw new MemberException(MemberErrorInfo.MEMBER_OAUTH_NOT_FOUND);
            return AuthenticatedMember.from(member);
        } else {
            MemberRole memberRole = findMemberRoleByRoleName("user");
            member = postMemberReqDto.createMember();
            member.setMemberRole(memberRole);
            return AuthenticatedMember.from(memberRepository.save(member));
        }
    }

    @Transactional
    public void saveTokenByMember(AuthenticatedMember authenticatedMember, String accessToken, String refreshToken) {
        Optional<MemberToken> memberTokenOptional = memberTokenRepository.findById(authenticatedMember.getMemberId());
        MemberToken memberToken;

        if (memberTokenOptional.isPresent()) {
            memberToken = memberTokenOptional.get();
            memberToken.changeAccessTokenByLogin(accessToken);
            memberToken.changeRefreshTokenByLogin(refreshToken);
        } else {
            Member member = memberRepository.findById(authenticatedMember.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
            memberToken = MemberToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .member(member)
                    .build();

            memberTokenRepository.save(memberToken);
        }
    }

    @Transactional
    public GetMemberResDto registerMemberInformation(AuthenticatedMember authenticatedMember, PostMemberInfoReqDto postMemberInfoReqDto) {
        boolean existNickname = memberRepository.existsByNickname(postMemberInfoReqDto.getNickname());
        if(existNickname) throw new MemberException(MemberErrorInfo.MEMBER_NICKNAME_DUPLICATION);
        Member member = memberRepository.findById(authenticatedMember.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
        MemberRole memberRole = member.getRole();
        if (postMemberInfoReqDto.getSsafyMember()) {
            member.setSSAFYMemberInformation(postMemberInfoReqDto, metaDataConsumer);
            return GetMemberResDto.fromSSAFYUser(member, memberRole);
        } else {
            member.setGeneralMemberInformation(postMemberInfoReqDto);
            return GetMemberResDto.fromGeneralUser(member, memberRole);
        }
    }

    @Transactional(readOnly = true)
    public MemberRole findMemberRoleByRoleName(String roleType) {
        return memberRoleRepository.findByRoleType(roleType).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_ROLE_TYPE_NOT_FOUND));
    }

    public boolean isInvalidOauthLogin(Member member, PostMemberReqDto postMemberReqDto) {
        OAuthType oAuthType = member.getOauthType();
        return !member.getOauthIdentifier().equals(postMemberReqDto.getOauthIdentifier()) || !oAuthType.isEqual(postMemberReqDto.getOauthName());
    }
    @Transactional(readOnly = true)
    public GetMemberResDto getMemberInformation(AuthenticatedMember authenticatedMember) {
        Member member = memberRepository.findById(authenticatedMember.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
        MemberRole memberRole = member.getRole();

        if (isNotInputMemberInformation(member)) {
            return GetMemberResDto.fromGeneralUser(member, memberRole);
        } else if(isGeneralMemberInformation(member)){
            return GetMemberResDto.fromGeneralUser(member, memberRole);
        } else if (isSSAFYMemberInformation(member)) {
            return GetMemberResDto.fromSSAFYUser(member, memberRole);
        }
        throw new MemberException(MemberErrorInfo.MEMBER_INFORMATION_ERROR);
    }

    @Transactional(readOnly = true)
    public PostNicknameResDto checkNicknamePossible(PostNicknameReqDto postNicknameReqDto) {
        boolean isExistNickname = memberRepository.existsByNickname(postNicknameReqDto.getNickname());
        if (isExistNickname) {
            throw new MemberException(MemberErrorInfo.MEMBER_NICKNAME_DUPLICATION);
        } else {
            return PostNicknameResDto.of(true);
        }
    }

    public boolean isNotInputMemberInformation(Member member) {
        return member.getSsafyMember() == null && member.getNickname() == null;
    }

    public boolean isGeneralMemberInformation(Member member) {
        return !member.getSsafyMember() && member.getNickname() != null;
    }

    public boolean isSSAFYMemberInformation(Member member) {
        return member.getSsafyMember() && member.getNickname() != null;
    }
}
