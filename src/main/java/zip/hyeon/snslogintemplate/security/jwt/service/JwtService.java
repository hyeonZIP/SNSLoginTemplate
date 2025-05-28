package zip.hyeon.snslogintemplate.security.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDTO;
import zip.hyeon.snslogintemplate.security.jwt.provider.ClaimType;
import zip.hyeon.snslogintemplate.security.jwt.provider.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.repository.RefreshTokenRepository;
import zip.hyeon.snslogintemplate.security.jwt.validator.JwtValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    public JwtProviderResponseDTO reIssueToken(Claims claims, String refreshTokenFromClient) {

        // 요청 들어온 AT 에서 userId와 userRole 꺼냄
        CustomUserDTO customUserDTO = getUserInfoFromClaims(claims);
        Long userId = customUserDTO.getUserId();
        String userRole = customUserDTO.getUserRole();

        // userId에 해당하는 RT 검색
        UserEntity userEntity = userRepository.getReferenceById(userId);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUser(userEntity)
                .orElseThrow(() -> new IllegalArgumentException("리프레쉬 토큰이 없습니다."));

        String refreshTokenFromDB = refreshTokenEntity.getRefreshToken();

        // DB RT 검증
        jwtValidator.getClaims(refreshTokenFromDB); // 서명/구조 검증

        // 클라이언트 RT와 DB RT 일치 비교
        boolean valid = jwtValidator.validateRefreshToken(refreshTokenFromClient, refreshTokenFromDB);

        if (!valid) {
            throw new AuthenticationServiceException("RefreshToken 불일치");
        }

        // 새 토큰 발급
        JwtProviderRequestDTO requestDTO = JwtProviderRequestDTO.of(userId, UserRole.valueOf(userRole));

        return jwtProvider.generateAccessTokenAndRefreshToken(requestDTO);
    }

    public JwtProviderResponseDTO generateAccessTokenAndRefreshToken(JwtProviderRequestDTO jwtProviderRequestDTO) {
        return jwtProvider.generateAccessTokenAndRefreshToken(jwtProviderRequestDTO);
    }

    @Transactional
    public void deleteOldAndNewRefreshToken(SaveRefreshTokenDTO saveRefreshTokenDTO) {
        log.info("삭제 쿼리 실행");
        deleteRefreshToken(saveRefreshTokenDTO.getUserId());
        log.info("저장 쿼리 실행");
        saveRefreshToken(saveRefreshTokenDTO);
    }

    public CustomUserDTO getUserInfoFromAccessToken(String accessToken) {
        Claims claims = jwtValidator.getClaims(accessToken);

        return getUserInfoFromClaims(claims);
    }

    public void saveRefreshTokenForOAuth2Login(SaveRefreshTokenDTO saveRefreshTokenDTO) {

        UserEntity userEntity = userRepository.getReferenceById(saveRefreshTokenDTO.getUserId());

        refreshTokenRepository.findByUser(userEntity)
                .ifPresentOrElse(
                        entity -> entity.updateRefreshToken(saveRefreshTokenDTO.getRefreshToken()),
                        () -> {
                            RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(saveRefreshTokenDTO.getRefreshToken(), userEntity);
                            refreshTokenRepository.save(refreshTokenEntity);
                        }
                );
    }

    private void saveRefreshToken(SaveRefreshTokenDTO saveRefreshTokenDTO) {

        UserEntity userEntity = userRepository.getReferenceById(saveRefreshTokenDTO.getUserId());

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(saveRefreshTokenDTO.getRefreshToken(), userEntity);

        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void deleteRefreshToken(Long userId) {

        UserEntity userEntity = userRepository.getReferenceById(userId);
        
        refreshTokenRepository.deleteByUser(userEntity);
        refreshTokenRepository.flush();
    }

    private CustomUserDTO getUserInfoFromClaims(Claims claims) {
        Long userId = claims.get(ClaimType.USER_ID.getKey(), Long.class);
        String userRole = claims.get(ClaimType.USER_ROLE.getKey(), String.class);

        return CustomUserDTO.of(userId, userRole);
    }

}
