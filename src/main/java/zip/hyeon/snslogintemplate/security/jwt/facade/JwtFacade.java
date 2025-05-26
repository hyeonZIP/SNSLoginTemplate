package zip.hyeon.snslogintemplate.security.jwt.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.refreshToken.dto.RefreshTokenDTO;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.refreshToken.service.RefreshTokenService;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.domain.user.service.UserService;
import zip.hyeon.snslogintemplate.security.jwt.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.JwtType;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtResponseDTO;

@Service
@RequiredArgsConstructor
public class JwtFacade {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public JwtResponseDTO issueToken(JwtRequestDTO jwtRequestDTO) {
        Long userId = jwtRequestDTO.getUserId();
        UserRole userRole = jwtRequestDTO.getUserRole();

        // AccessToken(AT), RefreshToken(RT) 발급
        String accessToken = jwtProvider.generateToken(userId, userRole, JwtType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.generateToken(userId, userRole, JwtType.REFRESH_TOKEN);

        return JwtResponseDTO.of(accessToken, refreshToken);
    }

    public void saveRefreshToken(RefreshTokenDTO refreshTokenDTO) {
        Long userId = refreshTokenDTO.getUserId();
        String refreshToken = refreshTokenDTO.getRefreshToken();

        UserEntity userEntity = userService.getReferenceById(userId);

        refreshTokenService.save(RefreshTokenEntity.issueRefreshToken(refreshToken, userEntity));
    }
}
