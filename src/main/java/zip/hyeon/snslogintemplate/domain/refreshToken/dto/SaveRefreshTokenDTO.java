package zip.hyeon.snslogintemplate.domain.refreshToken.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveRefreshTokenDTO {
    private Long userId;
    private String refreshToken;

    public static SaveRefreshTokenDTO of(Long userId, String refreshToken) {
        return new SaveRefreshTokenDTO(userId, refreshToken);
    }
}
