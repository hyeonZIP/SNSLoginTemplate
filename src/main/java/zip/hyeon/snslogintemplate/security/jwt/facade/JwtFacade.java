package zip.hyeon.snslogintemplate.security.jwt.facade;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.refreshToken.dto.RefreshTokenDTO;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.refreshToken.service.RefreshTokenService;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.service.UserService;
import zip.hyeon.snslogintemplate.security.jwt.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.JwtValidator;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtDTO;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtRequestDTO;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtFacade {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public JwtDTO generateAccessTokenAndRefreshToken(JwtRequestDTO dto) {
        return jwtProvider.generateAccessTokenAndRefreshToken(dto);
    }

    public void saveRefreshToken(RefreshTokenDTO refreshTokenDTO) {
        Long userId = refreshTokenDTO.getUserId();
        String refreshToken = refreshTokenDTO.getRefreshToken();

        UserEntity userEntity = userService.getReferenceById(userId);

        refreshTokenService.save(RefreshTokenEntity.issueRefreshToken(refreshToken, userEntity));
    }

    public CustomUserDetails getCustomUserDetails(JwtDTO jwtDTO) {

        String accessToken = jwtDTO.getAccessToken();
        String refreshToken = jwtDTO.getRefreshToken();

        Claims claims;
        try {
            jwtValidator.validateTokenEmpty(accessToken);
            claims = jwtValidator.getClaims(jwtDTO.getAccessToken());
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료 로직 동작");
            claims = e.getClaims();
            Long userId = claims.get("userId", Long.class);
            UserEntity userEntity = userService.getReferenceById(userId);
            Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenService.findByUser(userEntity);

            if (refreshTokenEntity.isPresent()) {

            }
        }

        Long userId = claims.get("userId", Long.class);
        String userRole = claims.get("userRole", String.class);

        return new CustomUserDetails(userId, userRole);
    }
}
