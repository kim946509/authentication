package store.agong.authentication.global.jwt.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieProvider {

    private final Duration refreshTokenMaxAge;

    public RefreshTokenCookieProvider(
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMillis
    ) {
        this.refreshTokenMaxAge = Duration.ofMillis(refreshTokenExpirationMillis);
    }

    public ResponseCookie create(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenMaxAge)
                .build();
    }

    public ResponseCookie delete() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }

}