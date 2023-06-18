package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppleOauthProvider implements OauthProvider {
    @Override
    public String getOauthUrl() {
        return null;
    }

    @Override
    public String getOauthAccessToken(String code) {
        return null;
    }

    @Override
    public PostMemberReqDto getMemberOauthIdentifier(String accessToken, String oauthName) {
        return null;
    }
}
