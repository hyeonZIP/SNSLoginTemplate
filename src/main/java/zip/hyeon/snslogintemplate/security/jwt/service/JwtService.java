package zip.hyeon.snslogintemplate.security.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.jwt.JwtValidator;
import zip.hyeon.snslogintemplate.security.jwt.provider.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    @Transactional
    public JwtProviderResponseDTO reIssueToken(Claims claims, String rtFromClient) {
        Long userId = claims.get("userId", Long.class);
        String userRole = claims.get("userRole", String.class);

        RefreshTokenEntity rtEntity = findRefreshToken(userId);
        String rtFromDB = rtEntity.getRefreshToken();

        jwtValidator.getClaims(rtFromDB); // 서명/구조 검증
        boolean valid = jwtValidator.validateRefreshToken(rtFromClient, rtFromDB);

        if (!valid) {
            throw new AuthenticationServiceException("RefreshToken 불일치");
        }

        // 새 토큰 발급 및 저장
        JwtProviderRequestDTO requestDTO = JwtProviderRequestDTO.of(userId, UserRole.valueOf(userRole));
        JwtProviderResponseDTO newTokens = jwtProvider.generateAccessTokenAndRefreshToken(requestDTO);

        deleteRefreshToken(userId);
        saveRefreshToken(SaveRefreshTokenDTO.of(userId, newTokens.getRefreshToken()));

        return newTokens;
    }

    public RefreshTokenEntity findRefreshToken(Long userId) {

        UserEntity userEntity = userRepository.getReferenceById(userId);

        return refreshTokenRepository.findByUser(userEntity)
                .orElseThrow(() -> new IllegalArgumentException("리프레쉬 토큰이 없습니다."));
    }

    public void saveRefreshToken(SaveRefreshTokenDTO saveRefreshTokenDTO) {

        Long userId = saveRefreshTokenDTO.getUserId();
        String refreshToken = saveRefreshTokenDTO.getRefreshToken();

        UserEntity userEntity = userRepository.getReferenceById(userId);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(refreshToken, userEntity);

        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void deleteRefreshToken(Long userId) {

        UserEntity userEntity = userRepository.getReferenceById(userId);

        refreshTokenRepository.deleteByUser(userEntity);
    }

}
