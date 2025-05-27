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

    /**
     * AccessToken과 RefreshToken은 항상 같이 발급된다.
     * 1. 회원가입시
     * 2. 리프레쉬 토큰 사용시
     */
    public static JwtProviderResponseDTO of(String accessToken, String refreshToken){
        return new JwtProviderResponseDTO(accessToken,refreshToken);
    }
}
