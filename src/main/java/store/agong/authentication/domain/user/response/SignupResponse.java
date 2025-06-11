package store.agong.authentication.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import store.agong.authentication.domain.user.enums.Role;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private String username;
    private String nickname;
    private Set<Role> roles;
}
