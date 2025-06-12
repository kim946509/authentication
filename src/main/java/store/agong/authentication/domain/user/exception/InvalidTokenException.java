package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않거나 만료되었습니다");
    }
}
