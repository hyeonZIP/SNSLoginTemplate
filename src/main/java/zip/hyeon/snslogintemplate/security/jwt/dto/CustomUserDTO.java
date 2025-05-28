package zip.hyeon.snslogintemplate.security.jwt.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDTO {
    private Long userId;
    private String userRole;

    public static CustomUserDTO of(Long userId, String userRole) {
        return new CustomUserDTO(userId, userRole);
    }
}
