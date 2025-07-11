package store.agong.authentication.global.exception;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.agong.authentication.global.response.ExceptionResponse;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(BaseException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(exceptionResponse);
    }

    // DTO @Valid 실패 시 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(". "));

        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }

}