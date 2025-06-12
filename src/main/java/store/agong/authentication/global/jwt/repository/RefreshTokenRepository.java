package store.agong.authentication.global.jwt.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(String username, String refreshToken);
    Optional<String> findByUsername(String username);
    void delete(String username);
}
