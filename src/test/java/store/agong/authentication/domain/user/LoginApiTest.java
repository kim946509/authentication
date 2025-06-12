package store.agong.authentication.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.repository.InMemoryUserRepository;
import store.agong.authentication.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class LoginApiTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        // given - 로그인 테스트 계정 생성
        User user = User.builder()
                .username("logintestuser")
                .password(passwordEncoder.encode("testpassword"))
                .nickname("로그인테스트유저")
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_success() throws Exception {
        // given
        String json = """
            {
              "username": "logintestuser",
              "password": "testpassword"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("로그인 - 실패: 비밀번호 불일치")
    void login_fail_invalidPassword() throws Exception {
        // given
        String json = """
            {
              "username": "loginuser",
              "password": "wrongpass"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(401);
    }
}
