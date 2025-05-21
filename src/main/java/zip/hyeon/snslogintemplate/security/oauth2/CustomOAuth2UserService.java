package zip.hyeon.snslogintemplate.security.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.userDetails.CustomOAuth2User;

import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("로드유저 실행 loadUser");
        // 1. 유저 정보 (attributes) 가져오기
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. CustomOAuth2User DTO 생성
        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.of(registrationId, attributes);

        // 4. 회원가입 및 로그인
        UserEntity user = getOrSave(oAuth2UserDTO);

        // 5. OAuth2User 반환
        return new CustomOAuth2User(user, attributes);
    }

    private UserEntity getOrSave(OAuth2UserDTO dto) {
        return userRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.register(dto.getName(), dto.getEmail(), dto.getProfile(), UserRole.ROLE_UNRANK);
                    return userRepository.save(newUser);
                });
    }
}
