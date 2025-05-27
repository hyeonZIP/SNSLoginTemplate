package zip.hyeon.snslogintemplate.security.jwt.refreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
