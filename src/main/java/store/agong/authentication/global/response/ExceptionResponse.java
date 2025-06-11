package store.agong.authentication.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.agong.authentication.global.exception.BaseException;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private Integer errorCode;
    private String message;

    public ExceptionResponse(BaseException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
    }
}
