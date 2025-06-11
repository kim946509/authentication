package store.agong.authentication.domain.user.repository;

import org.springframework.stereotype.Repository;
import store.agong.authentication.domain.user.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(store.get(username));
    }

    @Override
    public Optional<User> findActiveByUsername(String username) {
        User user = store.get(username);
        if (user == null || user.isDeleted()) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public User save(User user) {
        user.assignId(idSequence.getAndIncrement());
        user.markCreated(user.getUsername());
        store.put(user.getUsername(), user);
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        return store.containsKey(username);
    }

    @Override
    public long count() {
        return store.size();
    }
}
