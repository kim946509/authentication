package store.agong.authentication.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.agong.authentication.domain.user.dto.LoginDto;
import store.agong.authentication.domain.user.request.LoginRequest;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.domain.user.response.LoginResponse;
import store.agong.authentication.domain.user.response.SignupResponse;
import store.agong.authentication.domain.user.service.UserLoginService;
import store.agong.authentication.domain.user.service.UserSignupService;
import store.agong.authentication.global.jwt.provider.RefreshTokenCookieProvider;
import store.agong.authentication.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest request) {
        SignupResponse response = userSignupService.signup(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginDto loginDto = userLoginService.login(request);
        ResponseCookie refreshCookie = refreshTokenCookieProvider.create(loginDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(SuccessResponse.success(new LoginResponse(loginDto.getAccessToken())));
    }
}
