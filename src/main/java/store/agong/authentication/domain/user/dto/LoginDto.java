package store.agong.authentication.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    private String accessToken;
    private String refreshToken;
}