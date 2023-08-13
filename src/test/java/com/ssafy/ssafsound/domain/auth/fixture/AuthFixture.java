package com.ssafy.ssafsound.domain.auth.fixture;

import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import org.checkerframework.checker.units.qual.C;

import javax.servlet.http.Cookie;

public class AuthFixture {

    public static final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik" +
            "pvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    public static final String refreshToken = "syJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I" +
            "kpvaG4gRG9lIiwiaWF0IsdfserwetweSflKxwRJeSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    public static final MemberToken memberToken = MemberToken.builder().build();

    public static Cookie getAccessTokenCookie() {
        return new Cookie("accessToken", accessToken);
    }

    public static Cookie getRefreshTokenCookie() {
        return new Cookie("refreshToken", refreshToken);
    }
}
