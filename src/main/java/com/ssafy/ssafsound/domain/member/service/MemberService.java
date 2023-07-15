package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.*;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.*;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final MemberLinkRepository memberLinkRepository;
    private final MetaDataConsumer metaDataConsumer;
    @Value("${spring.constant.certification.CERTIFICATION_INQUIRY_TIME}")
    private Integer MAX_CERTIFICATION_INQUIRY_COUNT;
    @Value("${spring.constant.certification.MAX_MINUTES}")
    private Integer MAX_MINUTES;

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

    @Transactional
    public PostCertificationInfoResDto certifySSAFYInformation(AuthenticatedMember authenticatedMember, PostCertificationInfoReqDto postCertificationInfoReqDto) {
        Member member = memberRepository.findById(authenticatedMember.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        long minutes = getMinutesByDifferenceCertificationTryTime(member.getCertificationTryTime());
        if(minutes > MAX_MINUTES) member.initializeCertificationInquiryCount();
        if(member.getCertificationInquiryCount() >= MAX_CERTIFICATION_INQUIRY_COUNT) throw new MemberException(MemberErrorInfo.MEMBER_CERTIFICATED_FAIL);

        if (isValidCertification(postCertificationInfoReqDto)) {
            member.setCertificationState(AuthenticationStatus.CERTIFIED);
            member.setMajorTrack(metaDataConsumer.getMetaData(MetaDataType.MAJOR_TRACK.name(), postCertificationInfoReqDto.getMajorTrack()));
            return PostCertificationInfoResDto.of(true, member.getCertificationInquiryCount());
        } else {
            member.increaseCertificationInquiryCount();
            return PostCertificationInfoResDto.of(false, member.getCertificationInquiryCount());
        }
    }

    @Transactional
    public void registerMemberPortfolio(AuthenticatedMember authenticatedMember, PutMemberProfileReqDto putMemberProfileReqDto) {
        Member member = memberRepository.findById(authenticatedMember.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
        setMemberPortfolioIntroduceByMember(member, putMemberProfileReqDto);
        deleteExistMemberLinksAllByMemberAndSaveNewRequest(member, putMemberProfileReqDto.getMemberLinks());
        deleteExistMemberSkillsAllByMemberAndSaveNewRequest(member, putMemberProfileReqDto.getSkills());
    }

    public void deleteExistMemberLinksAllByMemberAndSaveNewRequest(Member member, List<PutMemberLink> memberLinks) {
        memberLinkRepository.deleteMemberLinksByMember(member);
        member.setMemberLinks(memberLinks);
    }

    public void deleteExistMemberSkillsAllByMemberAndSaveNewRequest(Member member, List<String> memberSkills) {
        memberSkillRepository.deleteMemberSkillsByMember(member);
        member.setMemberSkills(memberSkills, metaDataConsumer);
    }
    public void setMemberPortfolioIntroduceByMember(Member member, PutMemberProfileReqDto putMemberProfileReqDto) {
        if (putMemberProfileReqDto.getIntroduceMyself() != null) {
            memberProfileRepository.findMemberProfileByMember(member).ifPresentOrElse(
                    memberProfile -> memberProfile.changeIntroduceMyself(putMemberProfileReqDto.getIntroduceMyself()),
                    () -> memberProfileRepository.save(putMemberProfileReqDto.toMemberProfile(member))
            );
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

    private long getMinutesByDifferenceCertificationTryTime(LocalDateTime certificationTryTime) {
        if(certificationTryTime == null) return 0;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(certificationTryTime, currentTime);
        return duration.toMinutes();
    }
    
    private boolean isValidCertification(PostCertificationInfoReqDto postCertificationInfoReqDto) {
        MetaData information = metaDataConsumer.getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer().toLowerCase());
        return information.getId() == postCertificationInfoReqDto.getSemester();
    }
    
    private boolean isNotInputMemberInformation(Member member) {
        return member.getSsafyMember() == null && member.getNickname() == null && member.getMajor() == null;
    }

    private boolean isGeneralMemberInformation(Member member) {
        return !member.getSsafyMember() && member.getNickname() != null && member.getMajor() != null;
    }

    private boolean isSSAFYMemberInformation(Member member) {
        return member.getSsafyMember() && member.getNickname() != null && member.getMajor() != null;
    }
}
