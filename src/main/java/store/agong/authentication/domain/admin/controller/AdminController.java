package store.agong.authentication.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.agong.authentication.domain.admin.request.GrantAdminRequest;
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
            description = "유저에게 관리자 권한을 부여합니다. 이 API는 SUPER_ADMIN 권한을 가진 사용자만 사용할 수 있습니다."
    )
    @PatchMapping("/grant")
    public ResponseEntity<SuccessResponse<GrantAdminResponse>> grantAdmin(
            @RequestBody @Valid GrantAdminRequest request
    ) {
        GrantAdminResponse response = userGrantService.grantAdmin(request.getUsername());
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}

