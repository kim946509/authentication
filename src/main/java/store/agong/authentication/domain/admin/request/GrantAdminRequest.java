package store.agong.authentication.domain.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GrantAdminRequest {
    @Schema(
            description = "관리자 권한을 부여할 대상 유저의 username",
            example = "user"
    )
    @NotBlank(message = "username은 필수입니다.")
    private String username;
}
