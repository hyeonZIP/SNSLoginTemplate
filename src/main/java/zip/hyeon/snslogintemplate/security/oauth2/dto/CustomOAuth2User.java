package zip.hyeon.snslogintemplate.security.oauth2.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * CustomOAuth2UserService 에서 사용되는 클래스
 * SNS 로그인을 통해 Authentication 객체를 생성하기위한 클래스
 */
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDTO oAuth2UserDTO;
    private final UserEntity user;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_UNRANK.getUserRole()));
    }

    @Override
    public String getName() {
        return oAuth2UserDTO.getName();
    }

    public UserRole getUserRole() {
        return user.getRole();
    }

    public Long getUserId() {
        return user.getId();
    }

    public OAuth2UserDTO getOAuth2UserDTO() {
        return oAuth2UserDTO;
    }
}
