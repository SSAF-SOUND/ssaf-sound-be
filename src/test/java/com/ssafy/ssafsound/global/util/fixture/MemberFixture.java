package com.ssafy.ssafsound.global.util.fixture;

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

    public static final MemberProfile memberProfile = MemberProfile.builder()
            .introduce("안녕하세요! 반가워요.")
            .build();

    public static List<String> skills = new ArrayList<>(){
        {
            add("Java");
            add("Spring");
        }
    };

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
                .build();
    }

    public Member createGeneralMember() {
        return Member.builder()
                .id(1L)
                .role(memberRole)
                .nickname("james")
                .ssafyMember(false)
                .major(true)
                .build();
    }

    public SSAFYInfo createUncertifiedSSAFYInfo() {
        return SSAFYInfo.of(9, "서울",
                "UNCERTIFIED", null);
    }

    public SSAFYInfo createCertifiedSSAFYInfo() {
        return SSAFYInfo.of(9, "서울", "CERTIFIED", new MetaData(MajorTrack.JAVA));
    }

    public GetMemberResDto createCertifiedSSAFYMember() {
        return new GetMemberResDto(1L, "user", "paul", true, true,
                createCertifiedSSAFYInfo());
    }

    public GetMemberResDto createUncertifiedSSAFYMember() {
        return new GetMemberResDto(1L, "user", "james", true, true,
                createUncertifiedSSAFYInfo());
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
        return GetMemberPortfolioResDto.ofMemberProfile(createMember(), memberProfile);
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
