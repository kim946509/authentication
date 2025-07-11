package store.agong.authentication.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<T>("요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> SuccessResponse<T> success() {
        return new SuccessResponse<>("요청이 성공적으로 처리되었습니다.", null);
    }
}
