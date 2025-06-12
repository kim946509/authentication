package store.agong.authentication.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import store.agong.authentication.domain.admin.response.GrantAdminResponse;
import store.agong.authentication.domain.forcedReLogin.service.ForcedReLoginService;
import store.agong.authentication.domain.user.entity.User;
import store.agong.authentication.domain.user.repository.UserRepository;
import store.agong.authentication.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class UserGrantService {

    private final UserRepository userRepository;
    private final ForcedReLoginService forcedReLoginService;

    public GrantAdminResponse grantAdmin(String targetUsername) {
        User user = userRepository.findActiveByUsername(targetUsername)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        user.grantAdminRole();
        userRepository.update(user);
        forcedReLoginService.force(user.getUsername());

        return new GrantAdminResponse(user.getUsername(), user.getRoles());
    }
}
