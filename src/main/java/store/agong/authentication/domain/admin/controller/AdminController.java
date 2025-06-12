package store.agong.authentication.domain.admin.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserGrantService userGrantService;

    @PatchMapping("/grant")
    public ResponseEntity<SuccessResponse<GrantAdminResponse>> grantAdmin(
            @RequestBody @Valid GrantAdminRequest request
    ) {
        GrantAdminResponse response = userGrantService.grantAdmin(request.getUsername());
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
