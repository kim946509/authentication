package store.agong.authentication.domain.user.exception;

import org.springframework.http.HttpStatus;
import store.agong.authentication.global.exception.BaseException;

public class AlreadyAssignIdException extends BaseException {
    public AlreadyAssignIdException() {
        super(HttpStatus.BAD_REQUEST, "이미 ID가 할당된 사용자입니다.");
    }
}
