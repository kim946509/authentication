package store.agong.authentication.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.enums.Role;
import store.agong.authentication.domain.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // SUPER_ADMIN
        if (!userRepository.existsByUsername("superadmin")) {
            User superAdmin = User.builder()
                    .id(null)
                    .username("superadmin")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("슈퍼관리자")
                    .roles(new HashSet<>(Set.of(Role.SUPER_ADMIN)))
                    .build();
            userRepository.save(superAdmin);
        }

        // USER
        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                    .id(null)
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("일반유저")
                    .roles(new HashSet<>(Set.of(Role.USER)))
                    .build();
            userRepository.save(user);
        }

        // ADMIN
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .id(null)
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("관리자")
                    .roles(new HashSet<>(Set.of(Role.ADMIN)))
                    .build();
            userRepository.save(admin);
        }
    }
}
