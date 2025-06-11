package store.agong.authentication.domain.admin;

import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;

import java.util.Set;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.global.jwt.provider.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class GrantAdminApiTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("관리자 권한 부여 - 성공 (SUPER_ADMIN)")
    void grantAdmin_success() throws Exception {
        // given
        String superAdmin = "superAdmin";
        String targetUser = "targetUser";
        String accessToken = jwtTokenProvider.generateAccessToken(superAdmin, Set.of(Role.ADMIN, Role.USER));

        User user = User.builder()
                .username(targetUser)
                .password(passwordEncoder.encode("pass123"))
                .nickname("타겟")
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build();

        userRepository.save(user);

        String requestBody = """
        {
          "username": "targetUser"
        }
        """;


        // when
        MockHttpServletResponse response = mockMvc.perform(post("/admin/grant")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("targetUser", "ADMIN");
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 존재하지 않는 유저")
    void grantAdmin_fail_userNotFound() throws Exception {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken("superAdmin", Set.of(Role.SUPER_ADMIN));
        String requestBody = """
            {
              "username": "notExistUser"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/admin/grant")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - SUPER_ADMIN이 아님")
    void grantAdmin_fail_forbidden() throws Exception {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken("normalUser", Set.of(Role.USER));
        String requestBody = """
            {
              "username": "targetUser"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/admin/grant")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 비로그인 사용자")
    void grantAdmin_fail_unauthorized() throws Exception {
        // given
        String requestBody = """
            {
              "username": "targetUser"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/admin/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(403);
    }
}
