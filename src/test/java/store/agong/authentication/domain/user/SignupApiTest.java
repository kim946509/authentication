package store.agong.authentication.domain.user.controller;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class SignupApiTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 - 성공")
    void signup_success() throws Exception {
        // given
        String json = """
            {
              "username": "testesignup",
              "password": "password123",
              "nickname": "테스터"
            }
            """;

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @DisplayName("회원가입 - 실패: 중복 username")
    void signup_fail_duplicateUsername() throws Exception {
        // given
        String signupJson = """
            {
              "username": "duplicate",
              "password": "pass",
              "nickname": "중복"
            }
            """;

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andReturn();

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(400);
    }
}
