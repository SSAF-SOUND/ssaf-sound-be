package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.service.oauth.GoogleOauthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final GoogleOauthProvider googleOauthProvider;

    public String getUserAccessToken(CreateMemberReqDto createMemberReqDto) {
        if(createMemberReqDto.getOauthName().equals("google")) {
            googleOauthProvider.getOauthAccessToken(createMemberReqDto.getCode());
        }
        return null;
    }

    private String getUserGoogleIdentifier(String accessToken) {
        return googleOauthProvider.getUserOauthIdentifier(accessToken);
    }
}
