package zip.hyeon.snslogintemplate.security.jwt.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
