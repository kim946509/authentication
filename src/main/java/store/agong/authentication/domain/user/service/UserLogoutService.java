package store.agong.authentication.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class UserLogoutService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void logout(String username) {
        refreshTokenRepository.delete(username);
    }
}
