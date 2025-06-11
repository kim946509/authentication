package store.agong.authentication.global.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import store.agong.authentication.domain.user.enums.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

    private final String username;

    public UserAuthentication(String username, Set<Role> roles) {
        super(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet()));
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
