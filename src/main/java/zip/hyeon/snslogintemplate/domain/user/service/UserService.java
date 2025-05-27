package zip.hyeon.snslogintemplate.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.repository.UserRepository;
import zip.hyeon.snslogintemplate.security.oauth2.dto.OAuth2UserDTO;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getReferenceById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    public UserEntity findOrCreateUser(OAuth2UserDTO oAuth2UserDTO) {
        return userRepository.findByEmail(oAuth2UserDTO.getEmail())
                .orElseGet(() -> userRepository.save(UserEntity.createSocialLoginUser(oAuth2UserDTO)));
    }
}
