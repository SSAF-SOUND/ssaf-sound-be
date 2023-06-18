package com.ssafy.ssafsound.domain.auth.service.token;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenValidTime;
    private final long  refreshTokenValidTime;

    public JwtTokenProvider(
            @Value("${jwt.token.secret}")
            String key,
            @Value("${jwt.token.expire-access}")
            long accessTokenValidTime,
            @Value("${jwt.token.expire-refresh}")
            long refreshTokenValidTime) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    public String createAccessToken(AuthenticatedMember authenticatedMember) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
                .claim("id", authenticatedMember.getMemberId())
                .claim("role", authenticatedMember.getMemberRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String getPayload() {
        return null;
    }

    public AuthenticatedMember getParsedClaims(String token) {
        return null;
    }

    public boolean isValid(String token) {
        return false;
    }
}
