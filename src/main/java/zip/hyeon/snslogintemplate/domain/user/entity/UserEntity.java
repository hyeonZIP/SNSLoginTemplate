package zip.hyeon.snslogintemplate.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zip.hyeon.snslogintemplate.domain.global.BaseEntity;

@Entity
@Getter
@Table(name = "users")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    private static final String DEFAULT_NAME = "default name";
    private static final String DEFAULT_EMAIL = "default email";
    private static final String DEFAULT_PROFILE = "default profile";
    private static final String DEFAULT_LOGIN = "default login";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String profile;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String providerId;

    public static UserEntity createDefaultUser() {

        return UserEntity.builder()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL)
                .profile(DEFAULT_PROFILE)
                .role(UserRole.ROLE_UNRANK)
                .providerId(DEFAULT_LOGIN)
                .build();
    }
}
