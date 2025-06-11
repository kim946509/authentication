package store.agong.authentication.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.request.SignupRequest;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Set<Role> roles = new HashSet<>();

    public static User from(SignupRequest signupRequest) {
        return User.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build();
    }

    public static User of(Long id, String username, String password, String nickname, Set<Role> roles) {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .roles(roles)
                .build();
    }
}
