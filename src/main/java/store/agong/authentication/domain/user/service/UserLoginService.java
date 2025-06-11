package store.agong.authentication.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.user.dto.LoginDto;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.domain.user.request.LoginRequest;
import store.agong.authentication.domain.user.response.LoginResponse;
import store.agong.authentication.global.exception.BaseException;
import store.agong.authentication.global.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginDto login(LoginRequest request) {
        User user = userRepository.findActiveByUsername(request.getUsername())
                .orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getRoles());

        return new LoginDto(accessToken, refreshToken);
    }
}
