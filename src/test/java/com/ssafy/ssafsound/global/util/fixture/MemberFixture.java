package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.member.domain.*;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MajorTrack;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.time.LocalDateTime;

public class MemberFixture {

    private static final MemberRole memberRole = MemberRole.builder().id(1).roleType("user").build();

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
}
