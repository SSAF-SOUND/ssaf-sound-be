package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.event.MemberLeavedEvent;
import com.ssafy.ssafsound.domain.member.domain.AccountState;
import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberProfile;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberLinkRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberProfileRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberSkillRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.term.domain.MemberTermAgreement;
import com.ssafy.ssafsound.domain.term.domain.Term;
import com.ssafy.ssafsound.domain.term.repository.MemberTermAgreementRepository;
import com.ssafy.ssafsound.domain.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final MemberLinkRepository memberLinkRepository;
    private final TermRepository termRepository;
    private final MemberTermAgreementRepository memberTermAgreementRepository;
    private final MetaDataConsumer metaDataConsumer;
    private final MemberConstantProvider memberConstantProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public AuthenticatedMember createMemberByOauthIdentifier(PostMemberReqDto postMemberReqDto) {
        Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier());
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            if (isInvalidOauthLogin(member, postMemberReqDto)) {
                throw new MemberException(MemberErrorInfo.MEMBER_OAUTH_NOT_FOUND);
            } else if(isDeletedMember(member)) {
                throw new MemberException(MemberErrorInfo.MEMBER_DELETED);
            }
            return AuthenticatedMember.from(member);
        } else {
            MemberRole memberRole = findMemberRoleByRoleName("user");
            member = postMemberReqDto.createMember();
            member.setMemberRole(memberRole);
            return AuthenticatedMember.from(memberRepository.save(member));
        }
    }

    @Transactional
    public Member saveTokenByMember(
        AuthenticatedMember authenticatedMember,
        CreateMemberTokensResDto createMemberTokensResDto) {
        Member member = memberRepository.findById(authenticatedMember.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        Optional<MemberToken> memberTokenOptional = memberTokenRepository
            .findById(authenticatedMember.getMemberId());

        memberTokenOptional.ifPresentOrElse(
            memberToken -> changeMemberTokens(memberToken, createMemberTokensResDto),
                () -> createMemberToken(member, createMemberTokensResDto));

        return member;
    }

    @Transactional
    public GetMemberResDto registerMemberInformation(
            Long memberId,
            PostMemberInfoReqDto postMemberInfoReqDto) {
        boolean existNickname = memberRepository.existsByNickname(postMemberInfoReqDto.getNickname());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if(existNickname) {
            throw new MemberException(MemberErrorInfo.MEMBER_NICKNAME_DUPLICATION);
        } else if (isNotAgreedRequiredTerms(member, postMemberInfoReqDto.getTermIds())) {
            throw new MemberException(MemberErrorInfo.MEMBER_INPUT_INFORMATION_FAIL);
        }

        return member.registerMemberInformation(postMemberInfoReqDto, metaDataConsumer);
    }

    @Transactional
    public PostCertificationInfoResDto certifySSAFYInformation(
            Long memberId,
            PostCertificationInfoReqDto postCertificationInfoReqDto) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        long minutes = getMinutesByDifferenceCertificationTryTime(member.getCertificationTryTime());
        if(minutes > memberConstantProvider.getMAX_MINUTES()) {
            member.initializeCertificationInquiryCount();
        }
        if(member.getCertificationInquiryCount() >= memberConstantProvider.getCERTIFICATION_INQUIRY_TIME()) {
            throw new MemberException(MemberErrorInfo.MEMBER_CERTIFICATED_FAIL);
        }

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
    public void registerMemberPortfolio(
            Long memberId,
            PutMemberPortfolioReqDto putMemberPortfolioReqDto) {
        Member member = getMemberByMemberIdOrThrowException(memberId);
        setMemberPortfolioIntroduceByMember(member, putMemberPortfolioReqDto);
        deleteExistMemberLinksAllByMemberAndSaveNewRequest(member, putMemberPortfolioReqDto.getMemberLinks());
        deleteExistMemberSkillsAllByMemberAndSaveNewRequest(member, putMemberPortfolioReqDto.getSkills());
    }

    /**
     *  Member의 기본 정보 수정을 한다.
     * @author : YongsHub
     * @param : PatchMemberDefaultInfoReqDto : ssafyMember가 true라면 기수정보와 캠퍼스 정보가 필수임
     * @param : PatchMemberDefaultInfoReqDto :ssafyMember가 false라면 기수정보와 캠퍼스 정보가 필요하지 않음
     * @throws : ssafyMember가 true인데 semester가 null일 경우 SEMESTER_NOT_FOUND Exception,
     * @throws : memberId를 찾을 수 없을때 MEMBER_NOT_FOUND_BY_ID Exception
     */
    @Transactional
    public void patchMemberDefaultInfo(
            Long memberId,
            PatchMemberDefaultInfoReqDto patchMemberDefaultInfoReqDto) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        member.exchangeDefaultInformation(patchMemberDefaultInfoReqDto, metaDataConsumer);
    }

    @Transactional
    public void patchMemberPublicProfile(
            Long memberId,
            PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        member.exchangeProfilePublic(patchMemberPublicProfileReqDto.getIsPublic());
    }

    @Transactional
    public void changeMemberNickname(
            Long memberId,
            PatchMemberNicknameReqDto patchMemberNicknameReqDto) {
        boolean isExistNickname = memberRepository.existsByNickname(patchMemberNicknameReqDto.getNickname());
        Member member = getMemberByMemberIdOrThrowException(memberId);

        if (isExistNickname) {
            throw new MemberException(MemberErrorInfo.MEMBER_NICKNAME_DUPLICATION);
        }
        member.changeNickname(patchMemberNicknameReqDto.getNickname());
    }

    @Transactional
    public void changeMemberMajor(
            Long memberId,
            PatchMemberMajorReqDto patchMemberMajorReqDto) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        member.changeMajorStatus(patchMemberMajorReqDto.getIsMajor());
    }

    @Transactional
    public void changeMemberMajorTrack(Long memberId, String majorTrack) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        if(!member.getSsafyMember()) {
            throw new MemberException(MemberErrorInfo.MEMBER_NOT_SSAFY);
        }
        member.setMajorTrack(metaDataConsumer.getMetaData(MetaDataType.MAJOR_TRACK.name(), majorTrack));
    }

    @Transactional
    public void leaveMember(Long memberId) {
        Member member = getMemberByMemberIdOrThrowException(memberId);
        member.setAccountStateDeleted();
        member.changeNickname("@" + member.getId());
        applicationEventPublisher.publishEvent(new MemberLeavedEvent(member.getId()));
    }

    @Transactional(readOnly = true)
    public GetMemberPortfolioResDto getMyPortfolio(Long memberId) {
        Member member = memberRepository.findWithMemberLinksAndMemberSkills(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
        MemberProfile memberProfile = memberProfileRepository.findMemberProfileByMember(member).orElseGet(MemberProfile::new);

        return GetMemberPortfolioResDto.ofMemberProfile(member, memberProfile);
    }

    @Transactional(readOnly = true)
    public GetMemberPublicProfileResDto getMemberPublicProfileByMemberId(Long memberId) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        return GetMemberPublicProfileResDto.builder()
                .isPublic(member.getPublicProfile())
                .build();
    }

    @Transactional(readOnly = true)
    public MemberRole findMemberRoleByRoleName(String roleType) {
        return memberRoleRepository.findByRoleType(roleType).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_ROLE_TYPE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public GetMemberResDto getMemberInformation(Long memberId) {
        Member member = getMemberByMemberIdOrThrowException(memberId);

        if (isNotInputMemberInformation(member)) {
            return GetMemberResDto.fromGeneralUser(member);
        } else if(isGeneralMemberInformation(member)){
            return GetMemberResDto.fromGeneralUser(member);
        } else if (isSSAFYMemberInformation(member)) {
            return GetMemberResDto.fromSSAFYUser(member);
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

    @Transactional(readOnly = true)
    public GetMemberPortfolioResDto getMemberPortfolioById(Long memberId) {
        Member member = memberRepository.findWithMemberLinksAndMemberSkills(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (isPrivateOfMemberProfile(member)) {
            throw new MemberException(MemberErrorInfo.MEMBER_PROFILE_SECRET);
        } else if (isDeletedMember(member)) {
            throw new MemberException(MemberErrorInfo.MEMBER_DELETED);
        }
        MemberProfile memberProfile = memberProfileRepository.findMemberProfileByMember(member).orElseGet(MemberProfile::new);

        return GetMemberPortfolioResDto.ofMemberProfile(member, memberProfile);
    }

    @Transactional(readOnly = true)
    public GetMemberDefaultInfoResDto getMemberDefaultInfoByMemberId(Long memberId) {
        Member member = getMemberByMemberIdOrThrowException(memberId);
        return GetMemberDefaultInfoResDto.from(member);
    }

    public void deleteExistMemberLinksAllByMemberAndSaveNewRequest(Member member, List<PutMemberLink> memberLinks) {
        memberLinkRepository.deleteMemberLinksByMember(member);
        member.setMemberLinks(memberLinks);
    }

    public void deleteExistMemberSkillsAllByMemberAndSaveNewRequest(Member member, List<String> memberSkills) {
        memberSkillRepository.deleteMemberSkillsByMember(member);
        member.setMemberSkills(memberSkills, metaDataConsumer);
    }

    public void setMemberPortfolioIntroduceByMember(Member member, PutMemberPortfolioReqDto putMemberPortfolioReqDto) {
        if (putMemberPortfolioReqDto.getSelfIntroduction() != null) {
            memberProfileRepository.findMemberProfileByMember(member).ifPresentOrElse(
                    memberProfile -> memberProfile.changeSelfIntroduction(putMemberPortfolioReqDto.getSelfIntroduction()),
                    () -> memberProfileRepository.save(putMemberPortfolioReqDto.toMemberProfile(member))
            );
        }
    }

    private boolean isPrivateOfMemberProfile(Member member) {
        return !member.getPublicProfile();
    }

    private boolean isInvalidOauthLogin(Member member, PostMemberReqDto postMemberReqDto) {
        OAuthType oAuthType = member.getOauthType();
        return !member.getOauthIdentifier().equals(postMemberReqDto.getOauthIdentifier()) || !oAuthType.isEqual(postMemberReqDto.getOauthName());
    }

    private long getMinutesByDifferenceCertificationTryTime(LocalDateTime certificationTryTime) {
        if(certificationTryTime == null) return 0;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(certificationTryTime, currentTime);
        return duration.toMinutes();
    }
    
    private boolean isValidCertification(PostCertificationInfoReqDto postCertificationInfoReqDto) {
        MetaData information = metaDataConsumer.getMetaData(
                MetaDataType.CERTIFICATION.name(),
                postCertificationInfoReqDto.getAnswer().toLowerCase());

        if (postCertificationInfoReqDto.getSemester() == null) {
            return false;
        }
        return information.getId() == postCertificationInfoReqDto.getSemester();
    }
    
    private boolean isNotInputMemberInformation(Member member) {
        return member.getSsafyMember() == null && member.getNickname() == null && member.getMajor() == null;
    }

    private boolean isGeneralMemberInformation(Member member) {
        return !member.getSsafyMember() && member.getNickname() != null && member.getMajor() != null;
    }

    private boolean isSSAFYMemberInformation(Member member) {
        return member.getSsafyMember() && member.getNickname() != null && member.getMajor() != null
                && member.getSemester() != null && member.getCampus() != null && member.getCertificationState() != null;
    }

    private Member getMemberByMemberIdOrThrowException(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
    }

    private boolean isDeletedMember(Member member) {
        return member.getAccountState() == AccountState.DELETED;
    }

    private void changeMemberTokens(
        MemberToken memberToken,
        CreateMemberTokensResDto createMemberTokensResDto) {
        memberToken.changeAccessTokenByLogin(createMemberTokensResDto.getAccessToken());
        memberToken.changeRefreshTokenByLogin(createMemberTokensResDto.getRefreshToken());
    }

    private void createMemberToken(
        Member member,
        CreateMemberTokensResDto createMemberTokensResDto) {
        MemberToken memberToken = MemberToken.builder()
                .accessToken(createMemberTokensResDto.getAccessToken())
                .refreshToken(createMemberTokensResDto.getRefreshToken())
                .member(member)
                .build();
        memberTokenRepository.save(memberToken);
    }

    private boolean isNotAgreedRequiredTerms(Member member, Set<Long> termIds) {
        List<Term> terms = termRepository.getRequiredTerms();
        boolean allIdsExistInTermSequences = terms.stream()
                .map(Term::getId)
                .allMatch(termIds::contains);

        if (terms.size() != termIds.size()) {
            return true;
        } else if (!allIdsExistInTermSequences) {
            return true;
        } else {
            memberTermAgreementRepository.saveAll(MemberTermAgreement.ofMemberAndTerms(member, terms));
            return false;
        }
    }
}
