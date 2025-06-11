package store.agong.authentication.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.domain.user.response.SignupResponse;
import store.agong.authentication.domain.user.service.UserSignupService;
import store.agong.authentication.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserSignupService userSignupService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest request) {
        SignupResponse response = userSignupService.signup(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
