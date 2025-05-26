package zip.hyeon.snslogintemplate.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtDTO;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtRequestDTO;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtKeyManager jwtKeyManager;

    /*
        AccessToken & RefreshToken 생성
     */
    public JwtDTO generateAccessTokenAndRefreshToken(JwtRequestDTO dto) {
        String accessToken = generateToken(dto.getUserId(), dto.getUserRole(), JwtType.ACCESS_TOKEN);
        String refreshToken = generateToken(dto.getUserId(), dto.getUserRole(), JwtType.REFRESH_TOKEN);

        return JwtDTO.of(accessToken, refreshToken);
    }

    private String generateToken(Long userId, UserRole role, JwtType token) {
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
