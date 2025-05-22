package zip.hyeon.snslogintemplate.security.oauth2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.security.oauth2.dto.OAuth2UserDTO;
import zip.hyeon.snslogintemplate.security.oauth2.dto.CustomOAuth2User;

import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보 (attributes) 가져오기
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기 ex) kakao, google, naver
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2UserDTO 생성
        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.of(registrationId, attributes);

        // 4. OAuth2User 반환
        return new CustomOAuth2User(oAuth2UserDTO);
    }
}
