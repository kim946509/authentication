package store.agong.authentication.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.forcedReLogin.service.ForcedReLoginService;
import store.agong.authentication.domain.user.dto.LoginDto;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.exception.InvalidLoginExcepiton;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.domain.user.request.LoginRequest;
import store.agong.authentication.global.exception.BaseException;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ForcedReLoginService forcedReLoginService;

    public LoginDto login(LoginRequest request) {
        User user = userRepository.findActiveByUsername(request.getUsername())
                .orElseThrow(InvalidLoginExcepiton::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginExcepiton();
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getRoles());
        refreshTokenRepository.save(user.getUsername(), refreshToken);

        forcedReLoginService.unforce(user.getUsername());

        return new LoginDto(accessToken, refreshToken);
    }
}
