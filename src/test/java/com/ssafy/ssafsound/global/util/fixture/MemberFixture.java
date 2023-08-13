package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;

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
}
