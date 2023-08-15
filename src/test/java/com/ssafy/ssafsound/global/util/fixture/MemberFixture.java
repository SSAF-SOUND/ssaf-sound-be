package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.member.dto.GetMemberResDto;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MajorTrack;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.member.domain.*;

import java.time.LocalDateTime;

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

    public static final Member SSAFY_MEMBER = Member.builder()
            .id(1L)
            .role(memberRole)
            .nickname("paul")
            .ssafyMember(true)
            .major(false)
            .build();

    public static final SSAFYInfo SSAFY_INFO = SSAFYInfo
            .of(9, "서울", "CERTIFIED", new MetaData(MajorTrack.JAVA));

    public static final GetMemberResDto SSAFY_MEMBER_RESPONSE_DTO = new GetMemberResDto(
            1L, "user", "paul", true, true, SSAFY_INFO);

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
}
