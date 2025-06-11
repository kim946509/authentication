package store.agong.authentication.domain.user.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import lombok.NoArgsConstructor;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class SignupResponse {

    private String username;
    private String nickname;
    private Set<Role> roles;

    public static SignupResponse from(User user){
        return SignupResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .roles(user.getRoles())
                .build();
    }
}
