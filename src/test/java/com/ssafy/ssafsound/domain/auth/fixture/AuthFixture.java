package com.ssafy.ssafsound.domain.auth.fixture;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberAccessTokenResDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import org.springframework.http.ResponseCookie;

public class AuthFixture {

    public static final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik" +
            "pvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    public static final String refreshToken = "syJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
            "kpvaG4gRG9lIiwiaWF0IsdfserwetweSflKxwRJeSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    public static final MemberToken memberToken = MemberToken.builder().build();

    public static ResponseCookie getAccessTokenCookie() {
        return ResponseCookie.from("accessToken", accessToken).build();
    }

    public static AuthenticatedMember authenticatedMember = AuthenticatedMember
            .builder()
            .memberId(1L)
            .memberRole("user")
            .build();
    public static CreateMemberTokensResDto createMemberTokensResDto = CreateMemberTokensResDto
            .builder()
            .accessToken(AuthFixture.accessToken)
            .refreshToken(AuthFixture.refreshToken)
            .build();
    public static PostMemberReqDto postMemberReqDto = PostMemberReqDto.builder()
            .oauthName("github")
            .oauthIdentifier("sdfserwvdfereref")
            .build();

    public static CreateMemberAccessTokenResDto createMemberAccessTokenResDto = CreateMemberAccessTokenResDto.builder()
            .accessToken(accessToken)
            .build();

    public static CreateMemberReqDto getCreateMemberReqDto(String oauthName) {
        return CreateMemberReqDto.builder()
                .code("82309899c459f9aebeea")
                .oauthName(oauthName)
                .build();
    }
}
