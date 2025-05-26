package zip.hyeon.snslogintemplate.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zip.hyeon.snslogintemplate.exception.GlobalException;
import zip.hyeon.snslogintemplate.exception.ResultCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtKeyManager jwtKeyManager;

    /**
     *  토큰 빈 값 검증
     */
    public void validateTokenEmpty(String token){
        if (!StringUtils.hasText(token)){
            throw new GlobalException(ResultCode.TOKEN_NOT_FOUND);
        }
    }

    /**
     * 토큰 디코딩
     */
    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(jwtKeyManager.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*
        AccessToken 검증
        1. null 값 검증
     */
    public void validateAccessToken(String accessToken){

        if (accessToken == null || accessToken.trim().isEmpty()){
            throw new AuthenticationServiceException("AccessToken 이 공백입니다.");
        }
    }

    /*
        RefreshToken 검증
        1. null 값 검증
        2. DB의 RT와 클라이언트의 RT 비교
     */
    private boolean validateRefreshToken(String token1, String token2){
        if (token1 == null || token2 == null){
            return false;
        }

        byte[] bytes1 = token1.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = token2.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(bytes1, bytes2);

    }

}
