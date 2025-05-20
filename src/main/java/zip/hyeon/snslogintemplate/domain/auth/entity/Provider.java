package zip.hyeon.snslogintemplate.domain.auth.entity;

import lombok.Getter;

@Getter
public enum Provider {
    GITHUB("GITHUB"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String provider;

    Provider(String provider) {
        this.provider = provider;
    }
}
