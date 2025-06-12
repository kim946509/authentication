package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class AlreadyAdminUserException extends BaseException {

    public AlreadyAdminUserException() {
        super(HttpStatus.BAD_REQUEST, "이미 ADMIN 권한이 부여된 사용자입니다.");
    }
}
