package com.ssafy.ssafsound.domain.auth.service.token;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenManager {

    @Override
    public String createAccessToken(AuthenticatedUser authenticatedUser) {
        return null;
    }

    @Override
    public String createRefreshToken() {
        return null;
    }

    @Override
    public String getPayload() {
        return null;
    }

    @Override
    public AuthenticatedUser getParsedClaims(String token) {
        return null;
    }

    @Override
    public boolean isValid(String token) {
        return false;
    }
}
