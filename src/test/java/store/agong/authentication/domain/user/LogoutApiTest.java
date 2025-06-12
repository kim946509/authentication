package store.agong.authentication.domain.user;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import store.agong.authentication.domain.user.enums.Role;

import java.util.Set;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;
import store.agong.authentication.global.jwt.repository.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class LogoutApiTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("로그아웃 - 성공")
    void logout_success() throws Exception {
        // given
        String username = "logoutUser";
        String accessToken = jwtTokenProvider.generateAccessToken(username, Set.of(Role.USER));
        String refreshToken = jwtTokenProvider.generateRefreshToken(username, Set.of(Role.USER));

        refreshTokenRepository.save(username, refreshToken);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);

        String deletedToken = refreshTokenRepository.findByUsername(username).orElse(null);
        assertThat(deletedToken).isNull(); // 저장소에서 삭제됐는지 확인

        String setCookie = response.getHeader("Set-Cookie");
        assertThat(setCookie).isNotNull();
        assertThat(setCookie).contains("refreshToken=");
        assertThat(setCookie).contains("Max-Age=0");
    }
}
