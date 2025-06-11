package store.agong.authentication.global.jwt.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<String, String> store = new ConcurrentHashMap<>();

    @Override
    public void save(String username, String refreshToken) {
        store.put(username, refreshToken);
    }

    @Override
    public Optional<String> findByUsername(String username) {
        return Optional.ofNullable(store.get(username));
    }

    @Override
    public void delete(String username) {
        store.remove(username);
    }
}
