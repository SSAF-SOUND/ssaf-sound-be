package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberProfile;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberLinkRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberProfileRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberSkillRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.Certification;
import com.ssafy.ssafsound.domain.meta.domain.MajorTrack;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.term.repository.MemberTermAgreementRepository;
import com.ssafy.ssafsound.domain.term.repository.TermRepository;
import com.ssafy.ssafsound.global.util.fixture.MemberFixture;
import com.ssafy.ssafsound.global.util.fixture.TermFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
    private MemberProfileRepository memberProfileRepository;
    @Mock
    private MemberLinkRepository memberLinkRepository;
    @Mock
    private MemberSkillRepository memberSkillRepository;
    @Mock
    private TermRepository termRepository;
    @Mock
    private MemberTermAgreementRepository memberTermAgreementRepository;
    @Mock
    private MetaDataConsumer metaDataConsumer;
    @Mock
    private MemberConstantProvider memberConstantProvider;
    @Spy
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private MemberService memberService;
    MemberFixture memberFixture = new MemberFixture();
    TermFixture termFixture = new TermFixture();

    @Test
    @DisplayName("새로운 Oauth Identifier가 주어졌다면 멤버를 저장하는데 성공합니다.")
    void Given_OauthIdentifier_When_SaveMember_Then_Success() {
        //given
        Member member = memberFixture.createGeneralMember();
        PostMemberReqDto postMemberReqDto = memberFixture.createPostMemberReqDto();
        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier()))
                .willReturn(Optional.empty());
        given(memberRoleRepository.findByRoleType("user")).willReturn(Optional.of(member.getRole()));
        given(memberRepository.save(any())).willReturn(member);

        //when
        AuthenticatedMember response = memberService.createMemberByOauthIdentifier(postMemberReqDto);

        //then
        assertAll(
                () -> assertEquals(response.getMemberId(), member.getId()),
                () -> assertEquals(response.getMemberRole(), member.getRole().getRoleType())
        );

        //verify
        verify(memberRepository, times(1))
                .findByOauthIdentifier(eq(postMemberReqDto.getOauthIdentifier()));
        verify(memberRoleRepository, times(1)).findByRoleType(eq("user"));
        verify(memberRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("존재하는 Oauth Identifier와 일치한다면 해당 멤버를 가져옵니다.")
    void Given_ExistOauthIdentifier_When_FindMember_Then_Success() {
        //given
        Member member = memberFixture.createGeneralMember();
        PostMemberReqDto postMemberReqDto = memberFixture.createPostMemberReqDto();
        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier()))
                .willReturn(Optional.of(member));
        //when
        AuthenticatedMember response = memberService.createMemberByOauthIdentifier(postMemberReqDto);

        //then
        assertAll(
                () -> assertEquals(response.getMemberId(), member.getId()),
                () -> assertEquals(response.getMemberRole(), member.getRole().getRoleType())
        );

        verify(memberRepository).findByOauthIdentifier(eq(postMemberReqDto.getOauthIdentifier()));
    }

    @Test
    @DisplayName("존재하는 Oauth Identifier를 가져왔지만 요청된 정보와 일치하지 않다면 예외를 던진다.")
    void Given_OauthIdentifier_When_CompareIncorrectRequest_Then_ThrowException() {
        //given
        PostMemberReqDto postMemberReqDto = memberFixture.createPostMemberReqDto();
        Member member = memberFixture.createInitializerMember();
        given(memberRepository.findByOauthIdentifier(postMemberReqDto.getOauthIdentifier()))
                .willReturn(Optional.of(member));

        //when, then
        assertThrows(MemberException.class,
            () -> memberService.createMemberByOauthIdentifier(postMemberReqDto));

        //verify
        verify(memberRepository, times(1))
                .findByOauthIdentifier(postMemberReqDto.getOauthIdentifier());
    }

    @Test
    @DisplayName("Member가 토큰을 발급한 적이 없다면 토큰을 발급하고 저장한다.")
    void Given_Tokens_When_InitializeMember_Then_Success() {
        //given
        Member member = memberFixture.createInitializerMember();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        given(memberTokenRepository.findById(authenticatedMember.getMemberId()))
            .willReturn(Optional.empty());
        given(memberRepository.findById(authenticatedMember.getMemberId()))
            .willReturn(Optional.of(member));

        //when
        Member response = memberService
            .saveTokenByMember(authenticatedMember, memberFixture.createMemberTokensResDto());

        //then
        assertAll(
            () -> assertEquals(response.getId(), authenticatedMember.getMemberId()),
            () -> assertEquals(response.getRole().getRoleType(), authenticatedMember.getMemberRole())
        );


        //verify
        verify(memberTokenRepository, times(1))
            .findById(authenticatedMember.getMemberId());
        verify(memberRepository, times(1))
            .findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("Member가 토큰을 발급한 적이 있다면 새로운 토큰들로 저장한다.")
    void Given_Tokens_When_JoinedMember_Then_SuccessExchangeTokens() {
        //given
        MemberToken memberToken = memberFixture.createMemberToken();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberToken.getMember());
        given(memberRepository.findById(authenticatedMember.getMemberId()))
                .willReturn(Optional.of(memberToken.getMember()));
        given(memberTokenRepository.findById(authenticatedMember.getMemberId()))
                .willReturn(Optional.of(memberToken));

        //when
        Member response = memberService.saveTokenByMember(authenticatedMember, memberFixture.createMemberTokensResDto());

        //then
        assertAll(
                () -> assertEquals(response.getId(), authenticatedMember.getMemberId()),
                () -> assertEquals(response.getRole().getRoleType(), authenticatedMember.getMemberRole())
        );

        //verify
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
        verify(memberTokenRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("가입이 안된 Member라면 토큰 발급을 시도하면 예외가 발생한다.")
    void Given_Member_When_NotJoinedMember_Then_ThrowMemberException() {
        //given
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberFixture.createInitializerMember());
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        //when, then
        assertThrows(MemberException.class,
                () -> memberService.saveTokenByMember(authenticatedMember, memberFixture.createMemberTokensResDto()));

        //verify
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("이미 존재하는 닉네임이라면 닉네임 중복이라는 예외가 발생한다.")
    void Given_Nickname_When_ExistNickname_Then_ThrowMemberException() {
        //given
        PostNicknameReqDto postNicknameReqDto = memberFixture.createPostNicknameReqDto();
        given(memberRepository.existsByNickname(postNicknameReqDto.getNickname())).willReturn(true);

        //when, then
        assertThrows(MemberException.class, () -> memberService.checkNicknamePossible(postNicknameReqDto));

        //verify
        verify(memberRepository, times(1)).existsByNickname(eq("james"));
    }

    @Test
    @DisplayName("존재하지 않는 닉네임이라면 닉네임 사용을 할 수 있는 dto를 반환한다.")
    void Given_Nickname_When_NotExistNickname_Then_ReturnTrue() {
        //given
        PostNicknameReqDto postNicknameReqDto = memberFixture.createPostNicknameReqDto();
        given(memberRepository.existsByNickname(postNicknameReqDto.getNickname())).willReturn(false);

        //when
        PostNicknameResDto result = memberService.checkNicknamePossible(postNicknameReqDto);

        //then
        assertThat(result.isPossible()).isTrue();

        //verify
        verify(memberRepository, times(1))
                .existsByNickname(eq(postNicknameReqDto.getNickname()));
    }

    @Test
    @DisplayName("회원가입 이후 정보 입력을 하지 않았다면 닉네임과 싸피멤버여부에 대해 null을 가진 dto를 반환한다.")
    void Given_Member_When_GetMemberInfo_Then_Success() {
        //given
        Member member = memberFixture.createInitializerMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        GetMemberResDto response = memberService.getMemberInformation(member.getId());

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(GetMemberResDto.fromGeneralUser(member));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("회원가입 이후 싸피유저가 아닌 일반 유저에 대해 정보 입력을 했다면 일반 유저 dto를 반환한다.")
    void Given_Member_When_GetGeneralMemberInfo_Then_Success() {
        //given
        Member member = memberFixture.createGeneralMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        GetMemberResDto response = memberService.getMemberInformation(member.getId());

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(GetMemberResDto.fromGeneralUser(member));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("회원가입 이후 싸피유저에 대한 정보 입력을 했다면 싸피 유저 dto를 반환한다.")
    void Given_Member_When_GetSSAFYMemberInfo_Then_Success() {
        //given
        GetMemberResDto getMemberResDto = memberFixture.createCertifiedSSAFYMemberResDto();
        given(memberRepository.findById(getMemberResDto.getMemberId()))
                .willReturn(Optional.of(memberFixture.createCertifiedSSAFYMember()));
        //when
        GetMemberResDto response = memberService.getMemberInformation(getMemberResDto.getMemberId());

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(getMemberResDto);

        //verify
        verify(memberRepository, times(1)).findById(getMemberResDto.getMemberId());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 회원정보 입력에서 닉네임이 중복됐다면 예외가 발생한다.")
    void Given_ExistNickname_When_Register_MemberInfo_Then_ThrowMemberException() {
        //given
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberFixture.createGeneralMember());
        PostMemberInfoReqDto postMemberInfoReqDto = memberFixture.createPostGeneralMemberInfoReqDto();
        given(memberRepository.existsByNickname(postMemberInfoReqDto.getNickname())).willReturn(true);

        //when, then
        assertThrows(MemberException.class,
                () -> memberService.registerMemberInformation(authenticatedMember.getMemberId(), postMemberInfoReqDto));

        //verify
        verify(memberRepository, times(1)).existsByNickname(postMemberInfoReqDto.getNickname());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 회원정보 입력에서 회원을 찾을 수 없다면 예외가 발생한다.")
    void Given_NotExistMember_When_Register_MemberInfo_Then_ThrowMemberException() {
        //given
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberFixture.createGeneralMember());
        PostMemberInfoReqDto postMemberInfoReqDto = memberFixture.createPostGeneralMemberInfoReqDto();
        given(memberRepository.existsByNickname(postMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());

        //when, then
        assertThrows(MemberException.class,
                () -> memberService.registerMemberInformation(authenticatedMember.getMemberId(), postMemberInfoReqDto));

        //verify
        verify(memberRepository, times(1)).existsByNickname(postMemberInfoReqDto.getNickname());
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 일반 회원 정보에 대해 입력했다면 일반 회원정보 dto를 return 받는다.")
    void Given_PostGeneralMemberInfo_When_Register_MemberInfo_Then_ReturnGeneralMemberResDto() {
        //given
        Member member = memberFixture.createGeneralMember();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        PostMemberInfoReqDto postMemberInfoReqDto = memberFixture.createPostGeneralMemberInfoReqDto();
        given(memberRepository.existsByNickname(postMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));
        given(termRepository.getRequiredTerms()).willReturn(termFixture.createTerms(postMemberInfoReqDto.getTermIds()));

        //when
        GetMemberResDto response = memberService
                .registerMemberInformation(authenticatedMember.getMemberId(), postMemberInfoReqDto);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(GetMemberResDto.fromGeneralUser(member));

        //verify
        verify(memberRepository, times(1)).existsByNickname(postMemberInfoReqDto.getNickname());
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
        verify(termRepository, times(1)).getRequiredTerms();
        verify(memberTermAgreementRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("소셜 로그인 이후, 싸피 회원 정보에 대해 입력했다면 싸피 회원정보 dto를 return 받는다.")
    void Given_PostSSAFYMemberInfo_When_Register_MemberInfo_Then_ReturnSSAFYMemberResDto() {
        //given
        Member member = memberFixture.createCertifiedSSAFYMember();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        PostMemberInfoReqDto postMemberInfoReqDto = memberFixture.createPostSSAFYMemberInfoReqDto();
        given(memberRepository.existsByNickname(postMemberInfoReqDto.getNickname())).willReturn(false);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));
        given(metaDataConsumer.getMetaData(any(), any())).willReturn(new MetaData(Campus.SEOUL));

        //when
        GetMemberResDto response = memberService
                .registerMemberInformation(authenticatedMember.getMemberId(), postMemberInfoReqDto);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(GetMemberResDto.fromSSAFYUser(member));

        //verify
        verify(memberRepository, times(1)).existsByNickname(postMemberInfoReqDto.getNickname());
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("싸피생 인증 요청 시, 정답에 대한 요청을 했으면 성공한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_Success() {
        //given
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .majorTrack("Java")
                .semester(1)
                .answer("선물")
                .build();
        Member member = memberFixture.createUncertifiedSSAFYMember();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        given(metaDataConsumer.getMetaData(MetaDataType.MAJOR_TRACK.name(), "Java"))
                .willReturn(new MetaData(MajorTrack.JAVA));
        given(metaDataConsumer.getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer()))
                .willReturn(new MetaData(Certification.ONE_SEMESTER));
        given(memberRepository.findById(authenticatedMember.getMemberId()))
                .willReturn(Optional.of(member));
        given(memberConstantProvider.getCERTIFICATION_INQUIRY_TIME()).willReturn(5);
        given(memberConstantProvider.getMAX_MINUTES()).willReturn(5);

        //when
        PostCertificationInfoResDto response = memberService
                .certifySSAFYInformation(authenticatedMember.getMemberId(), postCertificationInfoReqDto);

        //then
        assertThat(response.isPossible()).isTrue();

        //verify
        verify(metaDataConsumer, times(1))
                .getMetaData(MetaDataType.MAJOR_TRACK.name(), "Java");
        verify(metaDataConsumer, times(1))
                .getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer());
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("싸피생 인증 요청 시, 정답에 대한 요청이 존재하지만 기수 정보가 다를 때 인증에 실패한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_Fail() {
        //given
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .majorTrack("Java")
                .semester(2)
                .answer("선물")
                .build();
        Member member = memberFixture.createUncertifiedSSAFYMember();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.of(member));
        given(metaDataConsumer.getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer().toLowerCase()))
                .willReturn(new MetaData(Certification.ONE_SEMESTER));
        given(memberConstantProvider.getMAX_MINUTES()).willReturn(5);
        given(memberConstantProvider.getCERTIFICATION_INQUIRY_TIME()).willReturn(5);

        //when
        PostCertificationInfoResDto response = memberService.certifySSAFYInformation(
                authenticatedMember.getMemberId(), postCertificationInfoReqDto);

        //then
        assertThat(response.isPossible()).isFalse();

        //verify
        verify(metaDataConsumer, times(1))
                .getMetaData(MetaDataType.CERTIFICATION.name(), postCertificationInfoReqDto.getAnswer().toLowerCase());
    }


    @Test
    @DisplayName("싸피생 인증 요청 시, 회원 정보를 찾을 수 없다면 예외가 발생한다.")
    void Given_PostCertificationInfo_When_Submit_SSAFY_Certification_Answer_Then_ThrowMemberException() {
        //given
        PostCertificationInfoReqDto postCertificationInfoReqDto = PostCertificationInfoReqDto.builder()
                .majorTrack("Java")
                .semester(1)
                .answer("선물")
                .build();
        AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberFixture.createUncertifiedSSAFYMember());
        given(memberRepository.findById(authenticatedMember.getMemberId())).willReturn(Optional.empty());

        //when, then
        assertThrows(MemberException.class, () -> memberService.certifySSAFYInformation(
                authenticatedMember.getMemberId(), postCertificationInfoReqDto));

        //verify
        verify(memberRepository, times(1)).findById(authenticatedMember.getMemberId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 수정 시도 시, 회원 정보를 찾을 수 없다면 예외가 발생한다")
    void Given_IsNotExistMember_When_Try_Change_Public_Profile_Then_ThrowMemberException() {
        //given
        PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto = memberFixture.createPatchMemberPublicProfileReqDto();
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

        //when, then
        assertThrows(MemberException.class,
                () -> memberService.patchMemberPublicProfile(member.getId(), patchMemberPublicProfileReqDto));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 수정에 성공한다.")
    void Given_Valid_PatchMemberPublicProfileReqDto_When_Try_Change_Public_Profile_Then_Success() {
        //given
        PatchMemberPublicProfileReqDto patchMemberPublicProfileReqDto = memberFixture.createPatchMemberPublicProfileReqDto();
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when, then
        memberService.patchMemberPublicProfile(member.getId(), patchMemberPublicProfileReqDto);

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 조회 시도 시, 회원 정보를 찾을 수 없다면 예외가 발생한다")
    void Given_IsNotExistMember_When_Get_Public_Profile_Then_ThrowMemberException() {
        //given
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

        //when, then
        assertThrows(MemberException.class,
                () -> memberService.getMemberPublicProfileByMemberId(member.getId()));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("마이 프로필 공개 여부 조회에 성공한다.")
    void Given_Valid_Member_When_Get_Public_Profile_Then_Success() {
        //given
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        GetMemberPublicProfileResDto response = memberService.getMemberPublicProfileByMemberId(member.getId());

        //then
        assertThat(response.getIsPublic()).isEqualTo(member.getPublicProfile());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }


    @Test
    @DisplayName("멤버의 포트폴리오 정보를 수정하는데 성공한다.")
    void Given_Valid_Member_Portfolio_Information_When_Put_Portfolio_Then_Success() {
        //given
        PutMemberPortfolioReqDto putMemberPortfolioReqDto = memberFixture.createPutMemberPortfolioReqDto();
        MemberProfile memberProfile = memberFixture.createMemberProfile();
        Member member = memberProfile.getMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(memberProfileRepository.findMemberProfileByMember(member)).willReturn(Optional.of(memberProfile));

        //when
        memberService.registerMemberPortfolio(member.getId(), putMemberPortfolioReqDto);

        //then
        assertThat(memberProfile.getIntroduce()).isEqualTo(putMemberPortfolioReqDto.getSelfIntroduction());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
        verify(memberLinkRepository, times(1)).deleteMemberLinksByMember(member);
        verify(memberSkillRepository, times(1)).deleteMemberSkillsByMember(member);
    }

    @Test
    @DisplayName("멤버의 닉네임을 수정하는데 성공한다.")
    void Given_Nickname_When_ChangeNickname_Then_Success() {
        //given
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        memberService.changeMemberNickname(member.getId(), memberFixture.createPatchMemberNicknameReqDto());

        //then
        assertThat(member.getNickname()).isEqualTo(memberFixture.createPatchMemberNicknameReqDto().getNickname());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("멤버는 전공자 여부를 수정하는데 성공한다.")
    void Given_Member_WhenChangeMajor_Then_Success() {
        //given
        PatchMemberMajorReqDto patchMemberMajorReqDto = memberFixture.createPatchMemberMajorReqDto();
        Member member = memberFixture.createMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        memberService.changeMemberMajor(member.getId(), patchMemberMajorReqDto);

        //then
        assertThat(member.getMajor()).isEqualTo(patchMemberMajorReqDto.getIsMajor());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("멤버 전공자 트랙을 수정하는데 성공합니다.")
    void Given_Member_WhenChangeMajorTrack_Then_Success() {
        //given
        PatchMemberMajorTrackReqDto patchMemberMajorTrackReqDto = memberFixture.createPatchMemberMajorTrackReqDto();
        Member member = memberFixture.createCertifiedSSAFYMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(metaDataConsumer.getMetaData(eq(MetaDataType.MAJOR_TRACK.name()), eq("Embedded")))
                .willReturn(new MetaData(MajorTrack.EMBEDDED));

        //when
        memberService.changeMemberMajorTrack(member.getId(), patchMemberMajorTrackReqDto.getMajorTrack());

        //then
        assertThat(member.getMajorTrack().getName()).isEqualTo(patchMemberMajorTrackReqDto.getMajorTrack());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
        verify(metaDataConsumer, times(1))
                .getMetaData(eq(MetaDataType.MAJOR_TRACK.name()), eq("Embedded"));
    }


    @Test
    @DisplayName("싸피생이 아닌 멤버가 전공 트랙 정보를 수정하려고 할 때 예외가 발생합니다.")
    void Given_NotSSAFYMember_WhenChangeMajorTrack_Then_Fail() {
        //given
        PatchMemberMajorTrackReqDto patchMemberMajorTrackReqDto = memberFixture.createPatchMemberMajorTrackReqDto();
        Member member = memberFixture.createGeneralMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when, then
        assertThrows(MemberException.class,
                () -> memberService
                        .changeMemberMajorTrack(member.getId(), patchMemberMajorTrackReqDto.getMajorTrack()));

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("회원 탈퇴 요청 시, 회원 탈퇴에 성공한다.")
    void Given_Member_When_leave_Then_Success() {
        //given
        Member member = memberFixture.createGeneralMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        memberService.leaveMember(member.getId());

        //then
        assertEquals("@" + member.getId(), member.getNickname());

        //verify
        verify(memberRepository, times(1)).findById(member.getId());
    }
}