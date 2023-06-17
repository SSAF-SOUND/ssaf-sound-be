package com.ssafy.ssafsound.domain.auth.service.token;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String createAccessToken(AuthenticatedUser authenticatedUser) {
        return null;
    }

    public String createRefreshToken() {
        return null;
    }

    public String getPayload() {
        return null;
    }

    public AuthenticatedUser getParsedClaims(String token) {
        return null;
    }

    public boolean isValid(String token) {
        return false;
    }
}
