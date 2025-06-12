package store.agong.authentication.domain.forcedReLogin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "강제 재로그인 테스트", description = "강제 재로그인 기능의 필터 동작을 확인하기 위한 테스트 API입니다.")
@RestController
@RequestMapping("/test")
public class ForcedReLoginTestController {

    @Operation(
            summary = "강제 재로그인 필터 테스트",
            description = """
            강제 재로그인 필터가 정상 동작하는지 확인하는 테스트용 API입니다.
            
            - 정상 유저의 경우: 200 OK와 함께 username 반환
            
            - 강제 재로그인 대상 유저의 경우:
             응답: 401 Unauthorized
            {
              "errorMessage": "재로그인이 필요합니다."
            }
            """
    )
    @GetMapping("/relogin")
    public ResponseEntity<String> protectedEndpoint(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok("허용된 로그인 : " + username);
    }
}
