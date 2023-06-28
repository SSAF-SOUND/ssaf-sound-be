package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberRoleRepository memberRoleRepository;
    @Mock
    private MemberTokenRepository memberTokenRepository;
    @Mock
    private MetaDataConsumer metaDataConsumer;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private MemberService memberService;
    PostMemberReqDto postMemberReqDto;
    Member member;
    MemberRole memberRole;

    @BeforeEach
    void setUp() {
        postMemberReqDto = PostMemberReqDto.builder()
                .oauthName("github")
                .oauthIdentifier("1232312312312")
                .build();

        memberRole = MemberRole.builder()
                .id(1)
                .roleType("user")
                .build();

        member = Member.builder()
                .oauthType(OAuthType.valueOf(postMemberReqDto.getOauthName().toUpperCase()))
                .oauthIdentifier(postMemberReqDto.getOauthIdentifier())
                .id(1L)
                .role(memberRole)
                .build();
    }

    @Test
    @DisplayName("새로운 Oauth Identifier가 주어졌다면 멤버를 저장하는데 성공합니다.")
    void Given_OauthIdentifier_When_SaveMember_Then_Success() {
        AuthenticatedMember authenticatedMemberReq = AuthenticatedMember.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .build();

        //given Mock Stub
        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier())).willReturn(Optional.empty());
        given(memberRoleRepository.findByRoleType("user")).willReturn(Optional.of(memberRole));
        given(memberRepository.save(argThat(member -> member.getOauthIdentifier().equals("1232312312312")))).willReturn(member);

        //when
        AuthenticatedMember authenticatedMemberRes = memberService.createMemberByOauthIdentifier(postMemberReqDto);

        //then
        assertAll(
                () -> assertThat(authenticatedMemberRes.getMemberId()).isEqualTo(authenticatedMemberReq.getMemberId()),
                () -> assertThat(authenticatedMemberRes.getMemberRole()).isEqualTo(authenticatedMemberReq.getMemberRole())
        );

        //verify
        verify(memberRepository).findByOauthIdentifier(eq(postMemberReqDto.getOauthIdentifier()));
        verify(memberRoleRepository).findByRoleType(eq("user"));
        verify(memberRepository).save(any());
    }
}