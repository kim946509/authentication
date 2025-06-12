package store.agong.authentication.domain.user;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ReissueApiTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private UserRepository userRepository;
    @Test
    @DisplayName("토큰 재발급 - 성공")
    void reissue_success() throws Exception {
        // given
        String username = "reissuer";
        Set<Role> roles = Set.of(Role.USER);

        userRepository.save(User.builder()
                .username(username)
                .password("test")
                .nickname("리이슈")
                .roles(roles)
                .build());

        String refreshToken = jwtTokenProvider.generateRefreshToken(username, Set.of(store.agong.authentication.domain.user.enums.Role.USER));
        refreshTokenRepository.save(username, refreshToken);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/reissue")
                        .cookie(new jakarta.servlet.http.Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        String responseBody = response.getContentAsString();
        assertThat(responseBody).contains("accessToken");

        String setCookieHeader = response.getHeader("Set-Cookie");
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("refreshToken=");

        refreshTokenRepository.delete(username);
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 잘못된 토큰 형식")
    void reissue_fail_invalidToken() throws Exception {
        // given
        String invalidToken = "this.is.not.valid";

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/reissue")
                        .cookie(new Cookie("refreshToken", invalidToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("토큰 재발급 실패 - AccessToken으로 시도")
    void reissue_fail_tokenTypeIsAccess() throws Exception {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken("accessfail", Set.of(Role.USER));

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/reissue")
                        .cookie(new Cookie("refreshToken", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 저장소에 없는 토큰")
    void reissue_fail_tokenNotInStore() throws Exception {
        // given
        String username = "nouser";
        String refreshToken = jwtTokenProvider.generateRefreshToken(username, Set.of(Role.USER));

        // 서버 저장소에 저장하지 않음 intentionally

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/reissue")
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 저장소에 저장된 토큰과 다름")
    void reissue_fail_tokenMismatch() throws Exception {
        // given
        String username = "mismatch";
        String validToken = jwtTokenProvider.generateRefreshToken(username, Set.of(Role.USER));
        String fakeToken = jwtTokenProvider.generateRefreshToken(username, Set.of(Role.SUPER_ADMIN));

        refreshTokenRepository.save(username, validToken);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/reissue")
                        .cookie(new Cookie("refreshToken", fakeToken)) // 다른 토큰
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
    }
}
