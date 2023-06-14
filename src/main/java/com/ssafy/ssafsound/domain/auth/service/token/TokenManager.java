package com.ssafy.ssafsound.domain.auth.service.token;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;

public interface TokenManager {
    String createAccessToken(AuthenticatedUser authenticatedUser);

    String createRefreshToken();

    String getPayload();

    AuthenticatedUser getParsedClaims(String token);

    boolean isValid(String token);
}
