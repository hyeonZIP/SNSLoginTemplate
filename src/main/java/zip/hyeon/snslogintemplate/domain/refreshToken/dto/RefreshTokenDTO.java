package zip.hyeon.snslogintemplate.domain.refreshToken.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenDTO {
    private Long userId;
    private String refreshToken;

    public static RefreshTokenDTO of(Long userId, String refreshToken) {
        return RefreshTokenDTO.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
    }
}
