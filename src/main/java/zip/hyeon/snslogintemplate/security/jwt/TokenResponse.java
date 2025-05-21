package zip.hyeon.snslogintemplate.security.jwt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    /**
     * AccessToken과 RefreshToken은 항상 같이 발급된다.
     * 1. 회원가입시
     * 2. 리프레쉬 토큰 사용시
     */
    public static TokenResponse of(String accessToken, String refreshToken){
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
