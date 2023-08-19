package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MajorTrack;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.member.domain.*;
import com.ssafy.ssafsound.domain.meta.domain.Skill;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;

public class MemberFixture {
    private static final MemberRole memberRole = MemberRole.builder().id(1).roleType("user").build();

    public static final Member INITIALIZER_MEMBER = Member.builder()
            .id(1L)
            .role(memberRole)
            .build();

    public static final Member GENERAL_MEMBER = Member.builder()
            .id(1L)
            .role(memberRole)
            .nickname("james")
            .ssafyMember(false)
            .major(true)
            .build();

    public static final SSAFYInfo CERTIFIED_SSAFY_INFO = SSAFYInfo
            .of(9, "서울", "CERTIFIED", new MetaData(MajorTrack.JAVA));

    public static final SSAFYInfo UNCERTIFIED_SSAFY_INFO = SSAFYInfo
            .of(9, "서울", "UNCERTIFIED", null);

    public static final GetMemberResDto CERTIFIED_SSAFY_MEMBER = new GetMemberResDto(
            1L, "user", "paul", true, true, CERTIFIED_SSAFY_INFO);

    public static final GetMemberResDto UNCERTIFIED_SSAFY_MEMBER = new GetMemberResDto(
            1L, "user", "james", true, true, UNCERTIFIED_SSAFY_INFO);

    public static final Member MEMBER_WALTER = Member.builder()
            .id(1L)
            .role(memberRole)
            .nickname("walter")
            .build();

    public static final Member MEMBER_SHERYL = Member.builder()
            .id(2L)
            .role(memberRole)
            .nickname("sheryl")
            .build();

    public static final Member MEMBER_KIM = Member.builder()
            .id(99L)
            .semester(9)
            .oauthIdentifier("oauth")
            .nickname("KIM")
            .accountState(AccountState.NORMAL)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .campus(new MetaData(Campus.SEOUL))
            .oauthType(OAuthType.GITHUB)
            .role(memberRole)
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .ssafyMember(true)
            .certificationInquiryCount(1)
            .certificationTryTime(LocalDateTime.now())
            .major(true)
            .publicProfile(true)
            .build();

    public static final PatchMemberMajorReqDto PATCH_IS_MAJOR_REQUEST = PatchMemberMajorReqDto.builder()
            .isMajor(false)
            .build();

    public static Set<MemberLink> memberLinks = new HashSet<>(){
        {
            add(MemberLink.builder().member(MEMBER_JAMES).linkName("velog").path("http://test-link1").build());
            add(MemberLink.builder().member(MEMBER_JAMES).linkName("t-story").path("http://test-link2").build());
        }
    };

    public static List<PutMemberLink> putMemberLinks = new ArrayList<>(){
        {
            add(PutMemberLink.builder().linkName("velog").path("http://test-link1").build());
            add(PutMemberLink.builder().linkName("t-story").path("http://test-link2").build());
        }
    };

    public static List<String> skills = new ArrayList<>(){
        {
            add("Java");
            add("Spring");
        }
    };

    public static Set<MemberSkill> memberSkills = new HashSet<>(){
        {
            add(MemberSkill.builder().member(MEMBER_JAMES).skill(new MetaData(Skill.JAVA)).build());
            add(MemberSkill.builder().member(MEMBER_JAMES).skill(new MetaData(Skill.ANDROID)).build());
        }
    };

    public static final MemberProfile memberProfile = MemberProfile.builder()
            .introduce("안녕하세요! 반가워요.")
            .build();

    public static final Member MEMBER_JAMES = Member.builder()
            .id(99L)
            .semester(9)
            .oauthIdentifier("oauth")
            .nickname("james")
            .accountState(AccountState.NORMAL)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .campus(new MetaData(Campus.SEOUL))
            .oauthType(OAuthType.GITHUB)
            .role(memberRole)
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .ssafyMember(true)
            .certificationInquiryCount(1)
            .certificationTryTime(LocalDateTime.now())
            .major(true)
            .publicProfile(true)
            .memberLinks(memberLinks)
            .memberSkills(memberSkills)
            .build();

    public static final GetMemberPortfolioResDto MY_PORTFOLIO = GetMemberPortfolioResDto
            .builder()
            .portfolioElement(PortfolioElement.of(MEMBER_JAMES, memberProfile))
            .build();

    public static final GetMemberDefaultInfoResDto GET_MEMBER_DEFAULT_INFO = GetMemberDefaultInfoResDto
            .builder()
            .nickname(MEMBER_JAMES.getNickname())
            .ssafyMember(MEMBER_JAMES.getSsafyMember())
            .isMajor(MEMBER_JAMES.getMajor())
            .ssafyInfo(CERTIFIED_SSAFY_INFO)
            .build();

    public static final PatchMemberDefaultInfoReqDto PATCH_MEMBER_DEFAULT_INFO_REQUEST = new PatchMemberDefaultInfoReqDto(false, null, null);

    public static final PatchMemberPublicProfileReqDto PATCH_MEMBER_PUBLIC_PROFILE_REQUEST = new PatchMemberPublicProfileReqDto(false);

    public static final GetMemberPublicProfileResDto GET_MEMBER_PUBLIC_PROFILE = GetMemberPublicProfileResDto
            .builder()
            .isPublic(true)
            .build();

    public static final PostNicknameReqDto POST_NICKNAME_REQUEST = PostNicknameReqDto
            .builder()
            .nickname("james")
            .build();

    public static final PostNicknameResDto POST_NICKNAME_RESPONSE = PostNicknameResDto
            .builder()
            .possible(true)
            .build();

    public static final PostCertificationInfoResDto POST_CERTIFICATION_INFO_RESPONSE = PostCertificationInfoResDto
            .builder()
            .possible(true)
            .certificationInquiryCount(3)
            .build();

    public static final PostCertificationInfoReqDto POST_CERTIFICATION_INFO_REQUEST = PostCertificationInfoReqDto
            .builder()
            .majorTrack("Java")
            .semester(9)
            .answer("great")
            .build();

    public static final PutMemberPortfolioReqDto PUT_MEMBER_PORTFOLIO_REQUEST = PutMemberPortfolioReqDto
            .builder()
            .selfIntroduction("안녕하세요 자기소개합니다.")
            .skills(skills)
            .memberLinks(putMemberLinks)
            .build();

    public static final Member MEMBER_TIM = Member.builder()
            .id(100L)
            .semester(9)
            .oauthIdentifier("oauth")
            .nickname("TIM")
            .accountState(AccountState.NORMAL)
            .certificationState(AuthenticationStatus.CERTIFIED)
            .campus(new MetaData(Campus.SEOUL))
            .oauthType(OAuthType.GITHUB)
            .role(memberRole)
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .ssafyMember(true)
            .certificationInquiryCount(1)
            .certificationTryTime(LocalDateTime.now())
            .major(true)
            .publicProfile(true)
            .build();

    public static final Member MEMBER_YONG = Member.builder()
            .id(3L)
            .semester(9)
            .nickname("yong")
            .certificationState(AuthenticationStatus.CERTIFIED)
            .role(memberRole)
            .campus(new MetaData(Campus.SEOUL))
            .majorTrack(new MetaData(MajorTrack.JAVA))
            .ssafyMember(true)
            .major(true)
            .build();
}
