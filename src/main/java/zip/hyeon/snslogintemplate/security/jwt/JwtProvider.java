package zip.hyeon.snslogintemplate.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtKeyManager jwtKeyManager;

    /**
     * 토큰 생성
     */
    public String generateToken(Long userId, UserRole role, JwtType token) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + token.getExpiredTime());

        return Jwts.builder()
                .claim("category", token.getCategory())
                .claim("userId", userId)
                .claim("userRole", role.getUserRole())
                .expiration(exp)
                .signWith(jwtKeyManager.getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }
}
