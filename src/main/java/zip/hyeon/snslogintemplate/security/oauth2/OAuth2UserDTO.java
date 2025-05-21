package zip.hyeon.snslogintemplate.security.oauth2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import zip.hyeon.snslogintemplate.exception.GlobalException;
import zip.hyeon.snslogintemplate.exception.ResultCode;

import java.util.Map;

/**
 * 각 SNS 로그인마다 제공해주는 규격이 다르기 때문에
 * SNS 로그인을 통해 받을 정보를 규격화 한다.
 */
@Slf4j
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2UserDTO {

    private String providerId;
    private String provider;
    private String name;
    private String email;
    private String profile;

    public static OAuth2UserDTO of(String registrationId, Map<String, Object> attributes) {
        return switch(registrationId){
            case "kakao" -> ofKakao(attributes);
//            case "google" -> ofGoogle(attributes);
//            case "github" -> ofGithub(attributes);
//            case "naver" -> ofNaver(attributes);
            default -> throw new GlobalException(ResultCode.INVALID_REGISTRATION_ID);
        };
    }

    //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
    private static OAuth2UserDTO ofKakao(Map<String, Object> attributes){
        Map<String, Object> account = (Map<String,Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String,Object>) account.get("profile");

        return OAuth2UserDTO.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }
}
