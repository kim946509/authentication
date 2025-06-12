package store.agong.authentication.domain.user.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.user.dto.ReissueDto;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.domain.user.response.ReissueResponse;
import store.agong.authentication.global.exception.BaseException;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class UserTokenReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public ReissueDto reissue(String refreshToken) {

        // 1. RefreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다.");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        // 2. 저장소에서 토큰 존재 여부 확인
        String storedRefreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다."));

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다.");
        }

        // 3. 최신 권한 정보 조회
        User user = userRepository.findActiveByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "유저 정보를 찾을 수 없습니다."));
        Set<Role> roles = user.getRoles();

        // 4. 재발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(username, roles);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username, roles);
        refreshTokenRepository.save(username, newRefreshToken);

        return new ReissueDto(newAccessToken, newRefreshToken);
    }
}