package zip.hyeon.snslogintemplate.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
