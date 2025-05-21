package zip.hyeon.snslogintemplate.security.jwt.refreshToken.entity;

import lombok.Getter;

@Getter
public enum RefreshTokenState {
    BLOCKED("BLOCKED"),
    USABLE("USABLE");

    private final String state;

    RefreshTokenState(String state){
        this.state = state;
    }
}
