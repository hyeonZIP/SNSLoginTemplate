package zip.hyeon.snslogintemplate.security.userDetails;

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

    private final UserEntity user;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getUserRole()));
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public Long getUserId() {
        return user.getId();
    }

    public UserRole getUserRole() {
        return user.getRole();
    }
}
