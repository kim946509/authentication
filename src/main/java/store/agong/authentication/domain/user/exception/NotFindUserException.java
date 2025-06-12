package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class NotFindUserException extends BaseException {
    public NotFindUserException() {
        super(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
    }
}
