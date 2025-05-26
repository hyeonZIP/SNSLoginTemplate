package zip.hyeon.snslogintemplate.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.auth.entity.AuthEntity;
import zip.hyeon.snslogintemplate.domain.auth.entity.Provider;
import zip.hyeon.snslogintemplate.domain.auth.repository.AuthRepository;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.oauth2.dto.CustomOAuth2User;
import zip.hyeon.snslogintemplate.security.oauth2.dto.OAuth2UserDTO;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보 (attributes) 가져오기
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기 ex) kakao, google, naver
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2UserDTO 생성
        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.of(registrationId, attributes);

        // 4. 기존 사용자 조회 또는 새로운 사용자 저장
        UserEntity userEntity = findOrCreateUser(oAuth2UserDTO);

        // 4. OAuth2User 반환
        return new CustomOAuth2User(oAuth2UserDTO, userEntity);
    }

    /**
     * Auth 정보 기반으로 기존 사용자 조회 또는 새로운 사용자 생성 및 저장
     */
    private UserEntity findOrCreateUser(OAuth2UserDTO oAuth2UserDTO) {

        Provider provider = oAuth2UserDTO.getProvider();
        String providerId = oAuth2UserDTO.getProviderId();

        Optional<AuthEntity> authEntity = authRepository.findByProviderAndProviderId(provider, providerId);

        if (authEntity.isPresent()) {
            UserEntity userEntity = authEntity.get().getUser();
            log.info("기존 사용자 불러오기 userId = {}", userEntity.getId());
            return userEntity;
        }

        return createTempUser(provider, providerId);
    }

    /**
     * Auth 객체 생성을 위해 defaultUser 생성
     */
    private UserEntity createTempUser(Provider provider, String providerId) {

        // 1. Auth 객체 생성을 위한 defaultUser 생성 및 저장
        UserEntity defaultUser = UserEntity.createDefaultUser();
        UserEntity savedUser = userRepository.save(defaultUser);

        // 2. Auth 객체 생성 및 저장
        AuthEntity authEntity = AuthEntity.newAuth(provider, providerId, savedUser);
        authRepository.save(authEntity);

        log.info("Auth 객체 생성 및 저장 provider = {}, providerId = {}", provider.getProvider(), providerId);
        log.info("새로운 사용자 생성 userId = {}", savedUser.getId());

        return savedUser;
    }
}
