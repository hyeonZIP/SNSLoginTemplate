package zip.hyeon.snslogintemplate.security.oauth2.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 각 SNS 로그인마다 제공해주는 규격이 다르기 때문에
 * SNS 로그인을 통해 받을 정보를 규격화 한다.
 */
@Slf4j
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2UserDTO {

    private static final String UNDER_BAR = "_";
    private String provider;
    private String name;
    private String profile;
    private String email;

    public static OAuth2UserDTO of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "kakao" -> ofKakao(attributes);
//            case "google" -> ofGoogle(attributes);
//            case "github" -> ofGithub(attributes);
//            case "naver" -> ofNaver(attributes);
            default -> throw new IllegalArgumentException("registrationId 에러 : " + registrationId);
        };
    }

    //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
    private static OAuth2UserDTO ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        log.info("attributes = {}", attributes);
        log.info("account = {}", account);
        log.info("profile = {}", profile);

        return OAuth2UserDTO.builder()
                .provider(Provider.KAKAO.getProvider() + UNDER_BAR + attributes.get("id"))
                .name(String.valueOf(profile.get("nickname")))
                .profile(String.valueOf(profile.get("profile_image_url")))
                .email(String.valueOf(account.get("email")))
                .build();
    }
}
