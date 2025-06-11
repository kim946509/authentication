package store.agong.authentication.domain.user.repository;

import store.agong.authentication.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
    long count();
}
