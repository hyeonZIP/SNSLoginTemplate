package zip.hyeon.snslogintemplate.security.jwt.validator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zip.hyeon.snslogintemplate.security.jwt.JwtKeyManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidator {

    private final JwtKeyManager jwtKeyManager;

    /**
     * 토큰 디코딩
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyManager.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateRefreshToken(String token1, String token2) {
        if (token1 == null || token2 == null) {
            return false;
        }

        byte[] bytes1 = token1.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = token2.getBytes(StandardCharsets.UTF_8);

        log.info("bytes1 = {}", bytes1);
        log.info("bytes2 = {}", bytes2);

        return MessageDigest.isEqual(bytes1, bytes2);

    }

}
