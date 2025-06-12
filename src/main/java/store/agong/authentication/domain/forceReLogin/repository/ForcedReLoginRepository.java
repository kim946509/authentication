package store.agong.authentication.domain.forceReLogin.repository;

public interface ForcedReLoginRepository {
    void save(String username);
    void delete(String username);
    boolean existsByUsername(String username);
}
