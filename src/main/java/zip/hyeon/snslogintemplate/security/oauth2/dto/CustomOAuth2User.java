package zip.hyeon.snslogintemplate.security.oauth2.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final Long userId;
    private final UserRole userRole;

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

    public Long getUserId(){
        return userId;
    }

    public UserRole getUserRole(){
        return userRole;
    }
}
