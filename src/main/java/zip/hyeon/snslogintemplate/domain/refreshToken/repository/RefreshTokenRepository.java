package zip.hyeon.snslogintemplate.domain.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
