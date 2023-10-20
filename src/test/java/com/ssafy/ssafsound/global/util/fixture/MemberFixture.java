package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.member.domain.*;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MajorTrack;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.Skill;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MemberFixture {

    private static final MemberRole memberRole = MemberRole.builder().id(1).roleType("user").build();

    public static List<String> skills = new ArrayList<>(){
        {
            add("Java");
            add("Spring");
        }
    };

    public MemberProfile createMemberProfile() {
        return MemberProfile.builder()
                .introduce("자기소개 하겠습니다.")
                .member(createMember())
                .build();
    }

    public Set<MemberLink> memberLinks = new HashSet<>(){
        {
            add(MemberLink.builder().member(createMember()).linkName("velog").path("http://test-link1").build());
            add(MemberLink.builder().member(createMember()).linkName("t-story").path("http://test-link2").build());
        }
    };

    public Set<MemberSkill> memberSkills = new HashSet<>(){
        {
            add(MemberSkill.builder().member(createMember()).skill(new MetaData(Skill.JAVA)).build());
            add(MemberSkill.builder().member(createMember()).skill(new MetaData(Skill.ANDROID)).build());
        }
    };

    public List<PutMemberLink> putMemberLinks = new ArrayList<>(){
        {
            add(PutMemberLink.builder().linkName("velog").path("http://test-link1").build());
            add(PutMemberLink.builder().linkName("t-story").path("http://test-link2").build());
        }
    };

    public Member createInitializerMember() {
        return Member.builder()
                .id(1L)
                .role(memberRole)
                .oauthType(OAuthType.GOOGLE)
                .oauthIdentifier("gimtaeyon@gmail.com")
                .build();
    }

    public Member createGeneralMember() {
        return Member.builder()
                .id(1L)
                .role(memberRole)
                .oauthType(OAuthType.GITHUB)
                .oauthIdentifier("1232312312312")
                .nickname("james")
                .ssafyMember(false)
                .major(true)
                .build();
    }

    public Member createCertifiedSSAFYMember() {
        return Member.builder()
                .id(1L)
                .role(memberRole)
                .oauthType(OAuthType.GITHUB)
                .oauthIdentifier("1232312312312")
                .nickname("james")
                .ssafyMember(true)
                .major(true)
                .campus(new MetaData(Campus.SEOUL))
                .majorTrack(new MetaData(MajorTrack.JAVA))
                .semester(9)
                .certificationState(AuthenticationStatus.CERTIFIED)
                .build();
    }

    public Member createUncertifiedSSAFYMember() {
        return Member.builder()
                .id(1L)
                .role(memberRole)
                .oauthType(OAuthType.GITHUB)
                .oauthIdentifier("1232312312312")
                .nickname("james")
                .ssafyMember(true)
                .major(true)
                .campus(new MetaData(Campus.SEOUL))
                .semester(9)
                .certificationState(AuthenticationStatus.UNCERTIFIED)
                .build();
    }

    public SSAFYInfo createUncertifiedSSAFYInfo() {
        return SSAFYInfo.of(9, "서울",
                "UNCERTIFIED", null);
    }

    public SSAFYInfo createCertifiedSSAFYInfo() {
        return SSAFYInfo.of(9, "서울", "CERTIFIED", new MetaData(MajorTrack.JAVA));
    }

    public GetMemberResDto createCertifiedSSAFYMemberResDto() {
        return GetMemberResDto.fromSSAFYUser(createCertifiedSSAFYMember());
    }

    public GetMemberResDto createUncertifiedSSAFYMemberResDto() {
        return GetMemberResDto.fromSSAFYUser(createUncertifiedSSAFYMember());
    }

    public PostMemberInfoReqDto createPostGeneralMemberInfoReqDto() {
        return PostMemberInfoReqDto.builder()
                .ssafyMember(false)
                .nickname("james")
                .isMajor(true)
                .termIds(new HashSet<>(Set.of(1L, 2L, 3L)))
                .build();
    }

    public PostMemberInfoReqDto createPostSSAFYMemberInfoReqDto() {
        return PostMemberInfoReqDto.builder()
                .ssafyMember(true)
                .nickname("james")
                .isMajor(true)
                .campus("서울")
                .semester(9)
                .build();
    }

    public Member createMember() {
        return Member.builder()
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
                .memberLinks(memberLinks)
                .memberSkills(memberSkills)
                .build();
    }

    public List<Member> createMemberList() {
        List<Member> members = new ArrayList<>();
        members.add(Member.builder()
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
                .build());

        members.add(Member.builder()
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
                .build());

        return members;
    }

    public GetMemberPortfolioResDto createGetMemberPortfolioResDto() {
        return GetMemberPortfolioResDto.ofMemberProfile(createMember(), createMemberProfile());
    }

    public PutMemberPortfolioReqDto createPutMemberPortfolioReqDto() {
        return PutMemberPortfolioReqDto
                .builder()
                .selfIntroduction("안녕하세요 자기소개합니다.")
                .skills(skills)
                .memberLinks(putMemberLinks)
                .build();
    }

    public PatchMemberMajorReqDto createPatchMemberMajorReqDto() {
        return PatchMemberMajorReqDto.builder()
                .isMajor(false)
                .build();
    }

    public PatchMemberMajorTrackReqDto createPatchMemberMajorTrackReqDto() {
        return PatchMemberMajorTrackReqDto.builder()
                .majorTrack("Embedded")
                .build();
    }

    public PostCertificationInfoReqDto createPostCertificationInfoReqDto() {
        return PostCertificationInfoReqDto.builder()
                .majorTrack("Java")
                .semester(9)
                .answer("great")
                .build();
    }

    public PostCertificationInfoResDto createPostCertificationInfoResDto() {
        return PostCertificationInfoResDto.builder()
                .possible(true)
                .certificationInquiryCount(3)
                .build();
    }

    public PostNicknameResDto createPostNicknameResDto() {
        return PostNicknameResDto.builder()
                .possible(true)
                .build();
    }

    public PostNicknameReqDto createPostNicknameReqDto() {
        return PostNicknameReqDto.builder()
                .nickname("james")
                .build();
    }

    public GetMemberPublicProfileResDto createGetMemberPublicProfileResDto() {
        return GetMemberPublicProfileResDto.builder()
                .isPublic(true)
                .build();
    }

    public PatchMemberPublicProfileReqDto createPatchMemberPublicProfileReqDto() {
        return new PatchMemberPublicProfileReqDto(false);
    }

    public PatchMemberDefaultInfoReqDto createPatchMemberDefaultInfoReqDto() {
        return new PatchMemberDefaultInfoReqDto(false, null, null);
    }

    public PostMemberReqDto createPostMemberReqDto() {
        return PostMemberReqDto.builder()
                .oauthName("github")
                .oauthIdentifier("1232312312312")
                .build();
    }

    public CreateMemberTokensResDto createMemberTokensResDto() {
        return CreateMemberTokensResDto.builder()
            .accessToken(AuthFixture.accessToken)
            .refreshToken(AuthFixture.refreshToken)
            .build();
    }

    public MemberToken createMemberToken() {
        return MemberToken.builder()
                .member(createGeneralMember())
                .accessToken(AuthFixture.accessToken)
                .refreshToken(AuthFixture.refreshToken)
                .build();
    }

    public PatchMemberNicknameReqDto createPatchMemberNicknameReqDto() {
        return new PatchMemberNicknameReqDto("james");
    }

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
}
