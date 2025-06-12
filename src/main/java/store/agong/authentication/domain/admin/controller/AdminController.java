package store.agong.authentication.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.agong.authentication.domain.admin.response.GrantAdminResponse;
import store.agong.authentication.domain.admin.service.UserGrantService;
import store.agong.authentication.global.response.SuccessResponse;

@Tag(name = "관리자", description = "관리자 전용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserGrantService userGrantService;

    @Operation(
            summary = "관리자 권한 부여",
            description = """
        유저에게 관리자 권한(ADMIN)을 부여합니다.
        이 API는 SUPER_ADMIN 권한을 가진 사용자만 호출할 수 있습니다.

        정상 응답 예시:
        {
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": {
            "username": "targetUser",
            "roles": [
              "USER",
              "ADMIN"
            ]
          }
        }

        예외 응답 예시 (권한 없음):
        {
          "errorMessage": "접근이 거부되었습니다"
        }

        예외 응답 예시 (존재하지 않는 유저):
        {
          "errorMessage": "해당 사용자를 찾을 수 없습니다."
        }
        
        예외 응답 예시 (존재하지 않는 유저):
        {
          "errorMessage": "이미 ADMIN 권한이 부여된 사용자입니다."
        }
        """
    )
    @PatchMapping("/users/{username}/roles")
    public ResponseEntity<SuccessResponse<GrantAdminResponse>> grantAdmin(
            @PathVariable("username") String username
    ) {
        GrantAdminResponse response = userGrantService.grantAdmin(username);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}

