package zip.hyeon.snslogintemplate.domain.refreshToken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.refreshToken.repository.RefreshTokenRepository;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshTokenEntity entity) {

        refreshTokenRepository.save(entity);

    }

    public Optional<RefreshTokenEntity> findByUser(UserEntity userEntity) {
        return refreshTokenRepository.findByUser(userEntity);
    }
}
