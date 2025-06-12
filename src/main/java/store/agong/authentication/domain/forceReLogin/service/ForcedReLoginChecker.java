package store.agong.authentication.domain.forceReLogin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import store.agong.authentication.domain.forceReLogin.repository.ForcedReLoginRepository;

import java.io.IOException;
import store.agong.authentication.global.response.ExceptionResponse;

@Component
@RequiredArgsConstructor
public class ForcedReLoginChecker {

    private final ForcedReLoginRepository forcedReLoginRepository;

    public boolean isForced(String username) {
        return forcedReLoginRepository.existsByUsername(username);
    }

    public void handleForced(HttpServletResponse response) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse("재로그인이 필요합니다.");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
    }
}
