package zip.hyeon.snslogintemplate.domain.auth.entity;

import lombok.Getter;

@Getter
public enum AuthSNS {
    GITHUB("GITHUB"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String sns;

    AuthSNS(String sns) {
        this.sns = sns;
    }
}
