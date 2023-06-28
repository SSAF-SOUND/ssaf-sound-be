package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Captor
    ArgumentCaptor<MemberToken> memberTokenArgumentCaptor;
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

    @Test
    @DisplayName("존재하는 Oauth Identifier와 일치한다면 해당 멤버를 가져옵니다.")
    void Given_ExistOauthIdentifier_When_FindMember_Then_Success() {
        AuthenticatedMember authenticatedMemberReq = AuthenticatedMember.builder()
                .memberId(member.getId())
                .memberRole(member.getRole().getRoleType())
                .build();

        //when
        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier())).willReturn(Optional.of(member));
        AuthenticatedMember authenticatedMemberRes = memberService.createMemberByOauthIdentifier(postMemberReqDto);

        //then
        assertAll(
                () -> assertThat(authenticatedMemberRes.getMemberId()).isEqualTo(authenticatedMemberReq.getMemberId()),
                () -> assertThat(authenticatedMemberRes.getMemberRole()).isEqualTo(authenticatedMemberReq.getMemberRole())
        );

        verify(memberRepository).findByOauthIdentifier(eq(postMemberReqDto.getOauthIdentifier()));
    }

    @Test
    @DisplayName("존재하는 Oauth Identifier를 가져왔지만 요청된 정보와 일치하지 않다면 예외를 던진다.")
    void Given_OauthIdentifier_When_CompareIncorrectRequest_Then_ThrowException() {
        PostMemberReqDto testPostMemberReqDto = PostMemberReqDto.builder()
                .oauthName("kakao")
                .oauthIdentifier(postMemberReqDto.getOauthIdentifier())
                .build();

        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier())).willReturn(Optional.of(member));

        assertThrows(MemberException.class, () -> memberService.createMemberByOauthIdentifier(testPostMemberReqDto));
    }

    @Test
    @DisplayName("Member가 토큰을 발급한 적이 없다면 토큰을 발급하고 저장한다.")
    void Given_Tokens_When_InitializeMember_Then_Success() {
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        String accessToken = jwtTokenProvider.createAccessToken(authenticatedMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(authenticatedMember);
        given(memberTokenRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        memberService.saveTokenByMember(authenticatedMember, accessToken, refreshToken);

        verify(memberTokenRepository).save(memberTokenArgumentCaptor.capture());
        MemberToken memberTokenRes = memberTokenArgumentCaptor.getValue();

        assertAll(
                () -> assertThat(memberTokenRes.getAccessToken()).isEqualTo(accessToken),
                () -> assertThat(memberTokenRes.getRefreshToken()).isEqualTo(refreshToken)
        );

        verify(memberTokenRepository).findById(authenticatedMember.getMemberId());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }
}