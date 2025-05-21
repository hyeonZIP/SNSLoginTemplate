package zip.hyeon.snslogintemplate.security.jwt;

import lombok.Getter;

@Getter
public enum JwtType {
    ACCESS_TOKEN("ACCESS_TOKEN", 60*30),// 30분
    REFRESH_TOKEN("REFRESH_TOKEN", 60*60*24*7); // 7일

    private final String category;
    private final long expiredTime;

    JwtType(String category, long expiredTime){
        this.category = category;
        this.expiredTime = expiredTime;
    }
}
