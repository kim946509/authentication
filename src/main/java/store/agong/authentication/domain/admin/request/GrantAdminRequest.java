package store.agong.authentication.domain.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GrantAdminRequest {
    @NotBlank(message = "username은 필수입니다.")
    private String username;
}
