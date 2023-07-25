package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.*;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.domain.*;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    @Mock
    private MemberConstantProvider memberConstantProvider;
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
    void Given_Member_When_GetMemberInfo_Then_Success() {
        GetMemberResDto getMemberResDto = GetMemberResDto.fromGeneralUser(member);
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
    void Given_Member_When_GetGeneralMemberInfo_Then_Success() {
        member.setGeneralMemberInformation(generalMemberInfoReqDto);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromGeneralUser(member);
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
    void Given_Member_When_GetSSAFYMemberInfo_Then_Success() {
        given(metaDataConsumer.getMetaData(MetaDataType.CAMPUS.name(), SSAFYMemberInfoReqDto.getCampus())).willReturn(new MetaData(Campus.SEOUL));
        member.setSSAFYMemberInformation(SSAFYMemberInfoReqDto, metaDataConsumer);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromSSAFYUser(member);
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

    @Test
    @DisplayName("소셜 로그인 이후, 회원정보 입력에서 닉네임이 중복됐다면 예외가 발생한다.")
    void Given_ExistNickname_When_Register_MemberInfo_Then_ThrowMemberException() {
        given(memberRepository.existsByNickname(generalMemberInfoReqDto.getNickname())).willReturn(true);

        assertThrows(MemberException.class, () -> memberService.registerMemberInformation(authenticatedMember, generalMemberInfoReqDto));

        verify(memberRepository).existsByNickname(generalMemberInfoReqDto.getNickname());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 회원정보 입력에서 회원을 찾을 수 없다면 예외가 발생한다.")
    void Given_NotExistMember_When_Register_MemberInfo_Then_ThrowMemberException() {
        given(memberRepository.existsByNickname(generalMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());

        assertThrows(MemberException.class, () -> memberService.registerMemberInformation(authenticatedMember, generalMemberInfoReqDto));

        verify(memberRepository).existsByNickname(generalMemberInfoReqDto.getNickname());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 일반 회원 정보에 대해 입력했다면 일반 회원정보 dto를 return 받는다.")
    void Given_PostGeneralMemberInfo_When_Register_MemberInfo_Then_ReturnGeneralMemberResDto() {
        member.setGeneralMemberInformation(generalMemberInfoReqDto);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromGeneralUser(member);
        given(memberRepository.existsByNickname(generalMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        GetMemberResDto result = memberService.registerMemberInformation(authenticatedMember, generalMemberInfoReqDto);

        assertThat(result).extracting(GetMemberResDto::getMemberId).isEqualTo(getMemberResDto.getMemberId());
        assertThat(result).extracting(GetMemberResDto::getSsafyMember).isEqualTo(getMemberResDto.getSsafyMember());
        assertThat(result).extracting(GetMemberResDto::getNickname).isEqualTo(getMemberResDto.getNickname());
        assertThat(result).extracting(GetMemberResDto::getIsMajor).isEqualTo(getMemberResDto.getIsMajor());

        verify(memberRepository).existsByNickname(generalMemberInfoReqDto.getNickname());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 싸피 회원 정보에 대해 입력했다면 싸피 회원정보 dto를 return 받는다.")
    void Given_PostSSAFYMemberInfo_When_Register_MemberInfo_Then_ReturnSSAFYMemberResDto() {
        given(metaDataConsumer.getMetaData(MetaDataType.CAMPUS.name(), SSAFYMemberInfoReqDto.getCampus())).willReturn(new MetaData(Campus.SEOUL));
        member.setSSAFYMemberInformation(SSAFYMemberInfoReqDto, metaDataConsumer);
        GetMemberResDto getMemberResDto = GetMemberResDto.fromSSAFYUser(member);
        given(memberRepository.existsByNickname(SSAFYMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));

        GetMemberResDto result = memberService.registerMemberInformation(authenticatedMember, SSAFYMemberInfoReqDto);

        assertThat(result).extracting(GetMemberResDto::getMemberId).isEqualTo(getMemberResDto.getMemberId());
        assertThat(result).extracting(GetMemberResDto::getSsafyMember).isEqualTo(getMemberResDto.getSsafyMember());
        assertThat(result).extracting(GetMemberResDto::getNickname).isEqualTo(getMemberResDto.getNickname());
        assertThat(result).extracting(GetMemberResDto::getIsMajor).isEqualTo(getMemberResDto.getIsMajor());
        assertThat(result).extracting(GetMemberResDto::getSsafyInfo).extracting(SSAFYInfo::getCampus).isEqualTo(getMemberResDto.getSsafyInfo().getCampus());
        assertThat(result).extracting(GetMemberResDto::getSsafyInfo).extracting(SSAFYInfo::getSemester).isEqualTo(getMemberResDto.getSsafyInfo().getSemester());

        verify(metaDataConsumer, times(2)).getMetaData(MetaDataType.CAMPUS.name(), "서울");
        verify(memberRepository).existsByNickname(generalMemberInfoReqDto.getNickname());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @ParameterizedTest
    @CsvSource({"Java, 1, ONE_SEMESTER, 선물", "Java, 2, TWO_SEMESTER, 하나", "Java, 3, THREE_SEMESTER, 출발", "Java, 4, FOUR_SEMESTER, 충전", "Java, 5, FIVE_SEMESTER, 극복",
            "Java, 6, SIX_SEMESTER, hot식스", "Java, 7, SEVEN_SEMESTER, 럭키", "Java, 8, EIGHT_SEMESTER, 칠전팔", "Java, 9, NINE_SEMESTER, great", "Java, 10, TEN_SEMESTER, 텐션"})
    @DisplayName("싸피생 인증 요청 시, 정답에 대한 요청을 했으면 성공한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_Success(String majorTrack, int semester, String name, String answer) {
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .majorTrack(majorTrack)
                .semester(semester)
                .answer(answer)
                .build();

        given(metaDataConsumer.getMetaData(MetaDataType.MAJOR_TRACK.name(), majorTrack))
                .willReturn(new MetaData(MajorTrack.JAVA));
        given(metaDataConsumer.getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer()))
                .willReturn(new MetaData(Certification.valueOf(name)));
        given(memberRepository.findById(authenticatedMember.getMemberId()))
                .willReturn(Optional.of(member));
        given(memberConstantProvider.getCERTIFICATION_INQUIRY_TIME()).willReturn(5);
        given(memberConstantProvider.getMAX_MINUTES()).willReturn(5);

        PostCertificationInfoResDto postCertificationInfoResDto = memberService
                .certifySSAFYInformation(authenticatedMember, postCertificationInfoReqDto);

        assertThat(member.getCertificationState()).isEqualTo(AuthenticationStatus.CERTIFIED);
        assertThat(member.getMajorTrack().getName()).isEqualTo(majorTrack);
        assertTrue(postCertificationInfoResDto.isPossible());

        verify(metaDataConsumer).getMetaData(MetaDataType.MAJOR_TRACK.name(), majorTrack);
        verify(metaDataConsumer).getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer());
        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @ParameterizedTest
    @CsvSource({"Java, 2, ONE_SEMESTER, 선물", "Java, 3, TWO_SEMESTER, 하나", "Java, 1, THREE_SEMESTER, 출발", "Java, 5, FOUR_SEMESTER, 충전", "Java, 6, FIVE_SEMESTER, 극복",
            "Java, 2, SIX_SEMESTER, hot식스", "Java, 4, SEVEN_SEMESTER, 럭키", "Java, 7, EIGHT_SEMESTER, 칠전팔", "Java, 8, NINE_SEMESTER, great", "Java, 9, TEN_SEMESTER, 텐션"})
    @DisplayName("싸피생 인증 요청 시, 정답에 대한 요청이 존재하지만 기수 정보가 다를 때 인증에 실패한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_Fail(String majorTrack, int semester, String name, String answer) {
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .majorTrack(majorTrack)
                .semester(semester)
                .answer(answer)
                .build();
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));
        given(metaDataConsumer.getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer().toLowerCase()))
                .willReturn(new MetaData(Certification.valueOf(name)));
        given(memberConstantProvider.getMAX_MINUTES()).willReturn(5);
        given(memberConstantProvider.getCERTIFICATION_INQUIRY_TIME()).willReturn(5);

        PostCertificationInfoResDto postCertificationInfoResDto = memberService.certifySSAFYInformation(authenticatedMember, postCertificationInfoReqDto);

        assertThat(postCertificationInfoResDto.isPossible()).isFalse();

        verify(metaDataConsumer).getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer().toLowerCase());
    }


    @Test
    @DisplayName("싸피생 인증 요청 시, 회원 정보를 찾을 수 없다면 예외가 발생한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_ThrowMemberException() {
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .semester(1)
                .answer("선물")
                .build();

        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());

        assertThrows(MemberException.class, () -> memberService.certifySSAFYInformation(authenticatedMember, postCertificationInfoReqDto));

        verify(memberRepository).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 수정 시도 시, 회원 정보를 찾을 수 없다면 예외가 발생한다")
    void Given_IsNotExistMember_When_Try_Change_Public_Profile_Then_ThrowMemberException() {
        //given
        PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto = new PatchMemberPublicProfileReqDto();
        given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

        //then
        assertThrows(MemberException.class,
                () -> memberService.patchMemberPublicProfile(member.getId(), patchMemberPublicProfileReqDto));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 수정에 성공한다.")
    void Given_Valid_PatchMemberPublicProfileReqDto_When_Try_Change_Public_Profile_Then_Success() {
        //given
        PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto = new PatchMemberPublicProfileReqDto(false);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //then
        memberService.patchMemberPublicProfile(member.getId(), patchMemberPublicProfileReqDto);

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 조회 시도 시, 회원 정보를 찾을 수 없다면 예외가 발생한다")
    void Given_IsNotExistMember_When_Get_Public_Profile_Then_ThrowMemberException() {
        //given
        given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

        //then
        assertThrows(MemberException.class,
                () -> memberService.getMemberPublicProfileByMemberId(member.getId()));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 조회에 성공한다.")
    void Given_Valid_Member_When_Get_Public_Profile_Then_Success() {
        //given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //then
        memberService.getMemberPublicProfileByMemberId(member.getId());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }
}