package store.agong.authentication.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class
SignupRequest {

    @NotBlank(message = "username은 필수입니다.")
    private String username;

    @NotBlank(message = "password는 필수입니다.")
    private String password;

    @NotBlank(message = "nickname은 필수입니다.")
    private String nickname;
}
