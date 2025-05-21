package zip.hyeon.snslogintemplate.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_UNRANK("ROLE_UNRANK"),
    ROLE_IRON("ROLE_IRON"),
    ROLE_BRONZE("ROLE_BRONZE"),
    ROLE_SILVER("ROLE_SILVER"),
    ROLE_GOLD("ROLE_GOLD"),
    ROLE_PLATINUM("ROLE_PLATINUM"),
    ROLE_EMERALD("ROLE_EMERALD"),
    ROLE_DIAMOND("ROLE_DIAMOND"),
    ROLE_MASTER("ROLE_MASTER"),
    ROLE_GRAND_MASTER("ROLE_GRAND_MASTER"),
    ROLE_CHALLENGER("ROLE_CHALLENGER");

    private final String userRole;

    UserRole(String userRole) {
        this.userRole = userRole;
    }
}
