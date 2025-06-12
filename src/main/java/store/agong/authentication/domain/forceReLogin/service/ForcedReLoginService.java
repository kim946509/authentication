package store.agong.authentication.domain.forceReLogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.forceReLogin.repository.ForcedReLoginRepository;

@Service
@RequiredArgsConstructor
public class ForcedReLoginService {

    private final ForcedReLoginRepository forcedReLoginRepository;

    public void force(String username) {
        forcedReLoginRepository.save(username);
    }

    public void unforce(String username) {
        forcedReLoginRepository.delete(username);
    }
}
