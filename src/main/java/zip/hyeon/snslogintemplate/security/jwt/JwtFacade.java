package zip.hyeon.snslogintemplate.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenState;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.service.RefreshTokenService;
import zip.hyeon.snslogintemplate.security.userDetails.CustomOAuth2User;

@Component
@RequiredArgsConstructor
public class JwtFacade {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    /**
     * AccessToken과 RefreshToken 생성
     */
    public TokenResponse generateTokenByOAuth(CustomOAuth2User customOAuth2User) {

        String accessToken = jwtProvider.generateToken(customOAuth2User.getUserId(), customOAuth2User.getUserRole(), JwtType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.generateToken(customOAuth2User.getUserId(), customOAuth2User.getUserRole(), JwtType.REFRESH_TOKEN);

        return TokenResponse.of(accessToken, refreshToken);
    }

    /**
     * RefreshToken 저장 로직
     */
    public void saveRefreshToken(Long userId, String refreshToken) {
        UserEntity user = userRepository.getReferenceById(userId);

        RefreshTokenEntity entity = RefreshTokenEntity.toEntity(refreshToken, user, RefreshTokenState.USABLE);

        refreshTokenService.save(entity);
    }
}
