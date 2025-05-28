package zip.hyeon.snslogintemplate.security.jwt.provider.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtProviderResponseDTO {
    private String accessToken;
    private String refreshToken;

    public static JwtProviderResponseDTO of(String accessToken, String refreshToken) {
        return new JwtProviderResponseDTO(accessToken, refreshToken);
    }
}
