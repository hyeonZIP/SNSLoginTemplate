package zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity;

import jakarta.persistence.Entity;
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
@Table(name = "refresh_token")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RefreshTokenState state;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static RefreshTokenEntity toEntity(String refreshToken, UserEntity user, RefreshTokenState state){
        return RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .user(user)
                .state(state)
                .build();
    }
}
