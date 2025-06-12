package store.agong.authentication.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import store.agong.authentication.domain.user.dto.LoginDto;
import store.agong.authentication.domain.user.dto.ReissueDto;
import store.agong.authentication.domain.user.request.LoginRequest;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.domain.user.response.LoginResponse;
import store.agong.authentication.domain.user.response.ReissueResponse;
import store.agong.authentication.domain.user.response.SignupResponse;
import store.agong.authentication.domain.user.service.UserLoginService;
import store.agong.authentication.domain.user.service.UserLogoutService;
import store.agong.authentication.domain.user.service.UserSignupService;
import store.agong.authentication.domain.user.service.UserTokenReissueService;
import store.agong.authentication.global.jwt.provider.RefreshTokenCookieProvider;
import store.agong.authentication.global.response.SuccessResponse;

@Tag(name = "유저", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;
    private final UserTokenReissueService userTokenReissueService;
    private final UserLogoutService userLogoutService;

    @Operation(
            summary = "회원가입",
            description = """
        회원가입을 진행합니다.
        중복된 username일 경우 예외가 발생합니다.
        
        정상 응답 예시:
        {
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": {
            "username": "testuser",
            "nickname": "테스터",
            "roles": [
              "USER"
            ]
          }
        }
        
        예외 응답 예시 (중복 username):
        {
          "errorMessage": "중복된 아이디입니다."
        }
        """
    )
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest request) {
        SignupResponse response = userSignupService.signup(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @Operation(
            summary = "로그인",
            description = """
        로그인 처리 후 AccessToken과 RefreshToken을 발급합니다.
        AccessToken은 응답 바디에, RefreshToken은 쿠키에 담깁니다.
        
        정상 응답 예시:
        {
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": {
            "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
          }
        }
     
        예외 응답 예시:
        {
          "errorMessage": "아이디 또는 비밀번호가 올바르지 않습니다."
        }
        """
    )
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginDto loginDto = userLoginService.login(request);
        ResponseCookie refreshCookie = refreshTokenCookieProvider.create(loginDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(SuccessResponse.success(new LoginResponse(loginDto.getAccessToken())));
    }


    @Operation(
            summary = "토큰 재발급",
            description = """
        RefreshToken을 통해 AccessToken을 재발급합니다.
        RefreshToken은 쿠키에서 가져오며, 응답으로 새로운 AccessToken을 반환합니다.
        
        정상 응답 예시:
        {
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": {
            "accessToken": "eyJhbGcfdsfdiOiJIUzI1NiIsInR..."
          }
        }
    
        예외 응답 예시:
        {
          "errorMessage": "토큰이 유효하지 않거나 만료되었습니다."
        }
        """
    )
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<ReissueResponse>> reissue(@CookieValue("refreshToken") String refreshToken) {
        ReissueDto tokens = userTokenReissueService.reissue(refreshToken);

        // refreshToken 쿠키로 재설정
        ResponseCookie cookie = refreshTokenCookieProvider.create(tokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(SuccessResponse.success(new ReissueResponse(tokens.getAccessToken())));
    }

    @Operation(
            summary = "로그아웃",
            description = """
        현재 로그인한 사용자의 RefreshToken을 무효화하고 쿠키에서 제거합니다.
        
        정상 응답 예시:
        {
          "message": "요청이 성공적으로 처리되었습니다.",
          "data": null
        }
        """
    )
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@AuthenticationPrincipal String username) {

        userLogoutService.logout(username);
        ResponseCookie deleteCookie = refreshTokenCookieProvider.delete();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(SuccessResponse.success());
    }

}
