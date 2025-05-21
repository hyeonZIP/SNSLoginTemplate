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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String profile;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static UserEntity register(String name, String email, String profile, UserRole role){
        return UserEntity.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .role(role)
                .build();
    }
}
