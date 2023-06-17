package com.ssafy.ssafsound.domain.auth.service.oauth;



public interface OauthProvider {

    String getOauthUrl();

    String getOauthAccessToken(String code);

    String getUserOauthIdentifier(String accessToken);
}
