package store.agong.authentication.domain.user.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.user.dto.ReissueDto;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.response.ReissueResponse;
import store.agong.authentication.global.exception.BaseException;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class UserTokenReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public ReissueDto reissue(String refreshToken) {

        // RefreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다.");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        Set<Role> roles = jwtTokenProvider.getRoles(refreshToken);

        String storedRefreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다."));

        // 저장된 RefreshToken과 비교
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다.");
        }

        // 재발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(username, roles);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username, roles);

        refreshTokenRepository.save(username, newRefreshToken); // 기존 토큰 대체

        return new ReissueDto(newAccessToken, newRefreshToken);
    }

}
