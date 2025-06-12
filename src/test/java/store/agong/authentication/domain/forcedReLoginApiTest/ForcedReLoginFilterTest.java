package store.agong.authentication.domain.forcedReLoginApiTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import store.agong.authentication.domain.forcedReLogin.repository.ForcedReLoginRepository;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ForcedReLoginFilterTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private ForcedReLoginRepository forcedReLoginRepository;

    private static final String TEST_ENDPOINT = "/test/relogin";

    @Test
    @DisplayName("강제 재로그인 - 차단되지 않음 (정상 유저)")
    void access_notForcedUser_success() throws Exception {
        // given
        String username = "normalUser";
        String accessToken = jwtTokenProvider.generateAccessToken(username, Set.of(Role.USER));

        // when
        var response = mockMvc.perform(get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("강제 재로그인 - 차단됨 (등록된 유저)")
    void access_forcedUser_fail() throws Exception {
        // given
        String username = "forcedUser";
        String accessToken = jwtTokenProvider.generateAccessToken(username, Set.of(Role.USER));
        forcedReLoginRepository.save(username);

        // when
        var response = mockMvc.perform(get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("재로그인이 필요합니다.");
    }
}
