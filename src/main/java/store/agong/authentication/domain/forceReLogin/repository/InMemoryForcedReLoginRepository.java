package store.agong.authentication.domain.forceReLogin.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryForcedReLoginRepository implements ForcedReLoginRepository {

    private final Set<String> forceReLoginUsers = ConcurrentHashMap.newKeySet();

    @Override
    public void save(String username) {
        forceReLoginUsers.add(username);
    }

    @Override
    public void delete(String username) {
        forceReLoginUsers.remove(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return forceReLoginUsers.contains(username);
    }
}
