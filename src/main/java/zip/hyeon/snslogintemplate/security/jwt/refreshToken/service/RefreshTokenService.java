package zip.hyeon.snslogintemplate.security.jwt.refreshToken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshTokenEntity entity){

        refreshTokenRepository.save(entity);

    }
}
