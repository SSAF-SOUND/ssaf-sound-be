package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

     public Member createMemberByOauthIdentifier(String oauthIdentifier){
         return Member.builder().build();
     }

    public CreateMemberTokensResDto createTokensByAuthenticatedUser(AuthenticatedUser authenticatedUser, String accessToken, String refreshToken) {
        return CreateMemberTokensResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
