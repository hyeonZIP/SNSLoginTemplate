package zip.hyeon.snslogintemplate.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zip.hyeon.snslogintemplate.domain.auth.entity.AuthEntity;
import zip.hyeon.snslogintemplate.domain.auth.entity.Provider;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByProviderAndProviderId(Provider provider, String providerId);
}
