package store.agong.authentication.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueDto {
    private String accessToken;
    private String refreshToken;
}
