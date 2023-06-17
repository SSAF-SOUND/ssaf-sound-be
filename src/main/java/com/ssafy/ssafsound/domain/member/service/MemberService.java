package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import com.ssafy.ssafsound.domain.member.repository.OAuthTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OAuthTypeRepository oauthTypeRepository;
    private final MemberRoleRepository memberRoleRepository;
    
    @Transactional
     public AuthenticatedUser createMemberByOauthIdentifier(PostMemberReqDto postMemberReqDto) {
         return AuthenticatedUser.builder().build();
     }

    public CreateMemberTokensResDto createTokensByAuthenticatedUser(AuthenticatedUser authenticatedUser, String accessToken, String refreshToken) {
        return CreateMemberTokensResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
