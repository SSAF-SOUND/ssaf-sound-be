package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{oauthName}")
    public void socialLoginRedirect(@PathVariable(name = "oauthName") String oauthName,
                                    HttpServletResponse response) {
        authService.sendRedirectURL(oauthName, response);
    }

    @PostMapping("/callback")
    public EnvelopeResponse<CreateMemberTokensResDto> login(
            @Valid @RequestBody CreateMemberReqDto createMemberReqDto) {
        String oauthIdentifier = authService.login(createMemberReqDto);
        Member member = memberService.createMemberByOauthIdentifier(oauthIdentifier);
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(member);

        CreateMemberTokensResDto createMemberTokensResDto = memberService.createTokensByAuthenticatedUser(
                authenticatedUser,
                jwtTokenProvider.createAccessToken(authenticatedUser),
                jwtTokenProvider.createRefreshToken());

        return EnvelopeResponse.<CreateMemberTokensResDto>builder()
                .data(createMemberTokensResDto)
                .build();
    }
}
