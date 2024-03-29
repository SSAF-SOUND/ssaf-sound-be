package com.ssafy.ssafsound.domain.auth.service.oauth;


import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;

public interface OauthProvider {

    String getOauthUrl();

    String getOauthAccessToken(String code);

    PostMemberReqDto getMemberOauthIdentifier(String accessToken, String oauthName);
}
