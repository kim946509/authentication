package store.agong.authentication.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.domain.user.response.SignupResponse;
import store.agong.authentication.global.exception.BaseException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }
        User user = User.from(request);
        User savedUser = userRepository.save(user);
        return new SignupResponse(savedUser.getUsername(), savedUser.getNickname(), savedUser.getRoles());
    }
}
