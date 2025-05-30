package zip.hyeon.snslogintemplate.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.service.UserService;
import zip.hyeon.snslogintemplate.security.oauth2.dto.CustomOAuth2User;
import zip.hyeon.snslogintemplate.security.oauth2.dto.OAuth2UserDTO;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보 (attributes) 가져오기
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기 ex) kakao, google, naver
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2UserDTO 생성
        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.of(registrationId, attributes);

        // 4. 기존 사용자 조회 또는 새로운 사용자 저장
        UserEntity userEntity = userService.findOrCreateUser(oAuth2UserDTO);

        // 4. OAuth2User 반환
        return new CustomOAuth2User(oAuth2UserDTO, userEntity.getId(), userEntity.getRole());
    }
}
