package zip.hyeon.snslogintemplate.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtKeyManager jwtKeyManager;

    public Long getUserId(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(jwtKeyManager.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return payload.get("user_id", Long.class);
    }

}
