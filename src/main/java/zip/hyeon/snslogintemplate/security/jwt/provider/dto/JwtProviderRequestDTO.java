package zip.hyeon.snslogintemplate.security.jwt.provider.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtProviderRequestDTO {
    private Long userId;
    private UserRole userRole;

    public static JwtProviderRequestDTO of(Long userId, UserRole userRole) {
        return new JwtProviderRequestDTO(userId, userRole);
    }
}
