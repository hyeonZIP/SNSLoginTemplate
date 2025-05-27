package zip.hyeon.snslogintemplate.domain.refreshToken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zip.hyeon.snslogintemplate.domain.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.refreshToken.repository.RefreshTokenRepository;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveRefreshToken(SaveRefreshTokenDTO saveRefreshTokenDTO) {

        Long userId = saveRefreshTokenDTO.getUserId();
        String refreshToken = saveRefreshTokenDTO.getRefreshToken();

        UserEntity userEntity = userRepository.getReferenceById(userId);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(refreshToken, userEntity);

        refreshTokenRepository.save(refreshTokenEntity);
    }

    public RefreshTokenEntity findRefreshToken(Long userId) {

        UserEntity userEntity = userRepository.getReferenceById(userId);

        return refreshTokenRepository.findByUser(userEntity)
                .orElseThrow(() -> new IllegalArgumentException("리프레쉬 토큰이 없습니다."));
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {

        UserEntity userEntity = userRepository.getReferenceById(userId);

        refreshTokenRepository.deleteByUser(userEntity);
    }

}
