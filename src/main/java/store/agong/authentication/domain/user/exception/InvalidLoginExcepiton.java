package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class InvalidLoginExcepiton extends BaseException {
    public InvalidLoginExcepiton() {
        super(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
