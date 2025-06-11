package store.agong.authentication.domain.admin.response;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.agong.authentication.domain.user.enums.Role;

@Getter
@AllArgsConstructor
public class GrantAdminResponse {
    private String username;
    private Set<Role> roles;
}
