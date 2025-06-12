package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class OverlappedUsernameException extends BaseException {
    public OverlappedUsernameException() {
        super(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
    }
}
