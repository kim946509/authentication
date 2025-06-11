package store.agong.authentication.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.agong.authentication.global.exception.BaseException;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String errorMessage;

    public ExceptionResponse(BaseException e) {
        this.errorMessage = e.getErrorMessage();
    }
}
