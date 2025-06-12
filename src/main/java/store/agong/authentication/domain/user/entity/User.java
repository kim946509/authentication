package store.agong.authentication.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.global.exception.BaseException;
import store.agong.authentication.global.util.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder
public class User extends BaseTimeEntity {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Set<Role> roles;

    public static User from(SignupRequest signupRequest) {
        return User.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build();
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new BaseException(HttpStatus.BAD_REQUEST,"이미 ID가 할당된 사용자입니다.");
        }
        this.id = id;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }

    public void grantAdminRole(){
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        if (this.roles.contains(Role.ADMIN)) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "이미 ADMIN 권한이 부여된 사용자입니다.");
        }
        this.roles.add(Role.ADMIN);
    }
}
