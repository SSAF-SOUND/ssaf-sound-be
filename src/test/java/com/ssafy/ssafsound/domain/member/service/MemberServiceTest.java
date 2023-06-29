package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
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
import static org.junit.jupiter.api.Assertions.*;
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
    PostMemberInfoReqDto generalMemberInfoReqDto;
    PostMemberInfoReqDto SSAFYMemberInfoReqDto;
    PostNicknameReqDto postNicknameReqDto;
    AuthenticatedMember authenticatedMember;

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

        generalMemberInfoReqDto = PostMemberInfoReqDto.builder()
                .ssafyMember(false)
                .nickname("taeyong")
                .isMajor(true)
                .build();

        SSAFYMemberInfoReqDto = PostMemberInfoReqDto.builder()
                .ssafyMember(true)
                .nickname("taeyong")
                .campus("서울")
                .isMajor(true)
                .semester(9)
                .build();

        postNicknameReqDto = new PostNicknameReqDto("taeyong");
        authenticatedMember = AuthenticatedMember.from(member);
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

    @Test
    @DisplayName("Member가 토큰을 발급한 적이 있다면 새로운 토큰들로 저장한다.")
    void Given_Tokens_When_JoinedMember_Then_SuccessExchangeTokens() {
        String accessToken = jwtTokenProvider.createAccessToken(authenticatedMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(authenticatedMember);
        MemberToken memberToken = MemberToken.builder()
                .member(member)
                .build();

        given(memberTokenRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(memberToken));

        memberService.saveTokenByMember(authenticatedMember, accessToken, refreshToken);

        verify(memberTokenRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("가입이 안된 Member라면 토큰 발급을 시도하면 예외가 발생한다.")
    void Given_Member_When_NotJoinedMember_Then_ThrowMemberException() {
        String accessToken = jwtTokenProvider.createAccessToken(authenticatedMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(authenticatedMember);

        given(memberTokenRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());

        assertThrows(MemberException.class, () -> memberService.saveTokenByMember(authenticatedMember, accessToken, refreshToken));

        verify(memberTokenRepository).findById(authenticatedMember.getMemberId());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("멤버가 싸피멤버라는 질문에 대해 null을 가지고 있고 닉네임도 null이라면 정보를 입력하지 않은지 검사하는 메서드에 대해 True를 반환한다.")
    void Given_Member_When_NotInputInformation_Then_ReturnTrue() {
        assertTrue(memberService.isNotInputMemberInformation(member));
    }

    @Test
    @DisplayName("멤버가 닉네임이나 싸피 멤버라는 질문에 대해 값을 가지고 있다면 정보를 입력하지 않은지 검사하는 메서드에 대해 False를 반환한다.")
    void Given_Member_when_InputInformation_Then_ReturnFalse() {
        member.setGeneralMemberInformation(generalMemberInfoReqDto);

        assertFalse(memberService.isNotInputMemberInformation(member));
    }

    @Test
    @DisplayName("일반 멤버이고 닉네임을 가지고 있다면 일반유저인지 확인하는 메서드에서 True를 반환한다.")
    void Given_Member_When_InputGeneralMember_Then_ReturnTrue() {
        member.setGeneralMemberInformation(generalMemberInfoReqDto);

        assertTrue(memberService.isGeneralMemberInformation(member));
    }

    @Test
    @DisplayName("멤버가 싸피 멤버이고 닉네임을 가지고 있다면 일반유저인지 확인하는 메서드에서 False를 반환한다.")
    void Given_Member_When_InputSSAFYMember_Then_ReturnFalse() {
        member.setSSAFYMemberInformation(SSAFYMemberInfoReqDto, metaDataConsumer);

        assertFalse(memberService.isGeneralMemberInformation(member));
    }

    @Test
    @DisplayName("멤버가 싸피 멤버이고 닉네임을 가지고 있다면 싸피유저인지 확인하는 메서드에서 True를 반환한다.")
    void Given_Member_When_InputSSAFYMember_Then_ReturnTrue() {
        member.setSSAFYMemberInformation(SSAFYMemberInfoReqDto, metaDataConsumer);

        assertTrue(memberService.isSSAFYMemberInformation(member));
    }

    @Test
    @DisplayName("이미 존재하는 닉네임이라면 닉네임 중복이라는 예외가 발생한다.")
    void Given_Nickname_When_ExistNickname_Then_ThrowMemberException() {
        given(memberRepository.existsByNickname(postNicknameReqDto.getNickname())).willReturn(true);

        assertThrows(MemberException.class, () -> memberService.checkNicknamePossible(postNicknameReqDto));

        verify(memberRepository).existsByNickname(eq("taeyong"));
    }

    @Test
    @DisplayName("존재하지 않는 닉네임이라면 닉네임 사용을 할 수 있는 dto를 반환한다.")
    void Given_Nickname_When_NotExistNickname_Then_ReturnTrue() {
        PostNicknameResDto postNicknameResDto = PostNicknameResDto.builder()
                .possible(true)
                .build();

        given(memberRepository.existsByNickname(postNicknameReqDto.getNickname())).willReturn(false);

        PostNicknameResDto result = memberService.checkNicknamePossible(postNicknameReqDto);

        assertThat(result.isPossible()).isEqualTo(postNicknameResDto.isPossible());

        verify(memberRepository).existsByNickname(eq("taeyong"));
    }

    @Test
    @DisplayName("회원가입 이후 정보 입력을 하지 않았다면 닉네임과 싸피멤버여부에 대해 null을 가진 dto를 반환한다.")
    void Given_Member_When_NotInputInformation_Then_ReturnDtoWithNullNicknameAndSSafyMember() {
        GetMemberResDto getMemberResDto = GetMemberResDto.fromGeneralUser(member, member.getRole());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        GetMemberResDto result = memberService.getMemberInformation(authenticatedMember);

        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(getMemberResDto.getMemberId()),
                () -> assertThat(result.getMemberRole()).isEqualTo(getMemberResDto.getMemberRole()),
                () -> assertThat(result.getSsafyMember()).isEqualTo(getMemberResDto.getSsafyMember()),
                () -> assertThat(result.getNickname()).isEqualTo(getMemberResDto.getNickname())
        );

        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("회원가입 이후 싸피유저가 아닌 일반 유저에 대해 정보 입력을 했다면 일반 유저 dto를 반환한다.")
    void Given_Member_When_InputGeneralInformation_Then_ReturnDtoWIthNicknameAndGeneralMember() {
        member.setGeneralMemberInformation(generalMemberInfoReqDto);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromGeneralUser(member, member.getRole());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        GetMemberResDto result = memberService.getMemberInformation(authenticatedMember);

        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(getMemberResDto.getMemberId()),
                () -> assertThat(result.getMemberRole()).isEqualTo(getMemberResDto.getMemberRole()),
                () -> assertThat(result.getSsafyMember()).isEqualTo(getMemberResDto.getSsafyMember()),
                () -> assertThat(result.getNickname()).isEqualTo(getMemberResDto.getNickname()),
                () -> assertThat(result.getIsMajor()).isEqualTo(getMemberResDto.getIsMajor())
        );
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("회원가입 이후 싸피유저에 대한 정보 입력을 했다면 싸피 유저 dto를 반환한다.")
    void Given_Member_When_InputSSAFYInformation_Then_ReturnDtoWithNicknameAndSSAFYMember() {
        given(metaDataConsumer.getMetaData(MetaDataType.CAMPUS.name(), SSAFYMemberInfoReqDto.getCampus())).willReturn(new MetaData(Campus.SEOUL));
        member.setSSAFYMemberInformation(SSAFYMemberInfoReqDto, metaDataConsumer);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromSSAFYUser(member, member.getRole());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        GetMemberResDto result = memberService.getMemberInformation(authenticatedMember);

        assertAll(
                () -> assertThat(result.getMemberId()).isEqualTo(getMemberResDto.getMemberId()),
                () -> assertThat(result.getMemberRole()).isEqualTo(getMemberResDto.getMemberRole()),
                () -> assertThat(result.getSsafyMember()).isEqualTo(getMemberResDto.getSsafyMember()),
                () -> assertThat(result.getNickname()).isEqualTo(getMemberResDto.getNickname()),
                () -> assertThat(result.getIsMajor()).isEqualTo(getMemberResDto.getIsMajor()),
                () -> assertThat(result.getSsafyInfo().getCampus()).isEqualTo(getMemberResDto.getSsafyInfo().getCampus()),
                () -> assertThat(result.getSsafyInfo().getSemester()).isEqualTo(getMemberResDto.getSsafyInfo().getSemester())
        );

        verify(metaDataConsumer).getMetaData(MetaDataType.CAMPUS.name(), "서울");
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }
}