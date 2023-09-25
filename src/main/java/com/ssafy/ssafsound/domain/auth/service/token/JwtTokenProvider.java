package com.ssafy.ssafsound.domain.auth.service.token;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import io.jsonwebtoken.*;
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
                .claim("memberId", authenticatedMember.getMemberId())
                .claim("memberRole", authenticatedMember.getMemberRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(AuthenticatedMember authenticatedMember) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                .claim("memberId", authenticatedMember.getMemberId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public Long getMemberIdByRefreshToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorInfo.AUTH_TOKEN_EXPIRED);
        }
        return claims.get("memberId", Long.class);
    }

    public AuthenticatedMember getParsedClaimsByAccessToken(String accessToken) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorInfo.AUTH_TOKEN_EXPIRED);
        }
        Long memberId = claims.get("memberId", Long.class);
        String memberRole = claims.get("memberRole", String.class);

        return AuthenticatedMember.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .build();
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
