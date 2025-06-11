package store.agong.authentication.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.domain.user.request.SignupRequest;
import store.agong.authentication.domain.user.response.SignupResponse;
import store.agong.authentication.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class UserSignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }
        User user = User.from(request);
        user.encodePassword(passwordEncoder);
        return SignupResponse.from(userRepository.save(user));
    }
}
