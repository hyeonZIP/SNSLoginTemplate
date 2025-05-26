package zip.hyeon.snslogintemplate.security.jwt.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtRequestDTO {
    private Long userId;
    private UserRole userRole;

    public static JwtRequestDTO of(Long userId, UserRole userRole) {
        return JwtRequestDTO.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
    }
}
