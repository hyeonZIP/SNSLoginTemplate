package zip.hyeon.snslogintemplate.domain.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zip.hyeon.snslogintemplate.domain.global.BaseEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;

@Entity
@Table(name = "auth")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    @Enumerated(EnumType.STRING)
    private AuthSNS sns;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static AuthEntity toEntity(String provider, AuthSNS sns) {
        return AuthEntity.builder()
                .provider(provider)
                .sns(sns)
                .build();
    }
}
