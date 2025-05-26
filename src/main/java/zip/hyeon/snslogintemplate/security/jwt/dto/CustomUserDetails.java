package zip.hyeon.snslogintemplate.security.jwt.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zip.hyeon.snslogintemplate.domain.user.entity.UserEntity;

import java.util.Collection;

/**
 * SNS 로그인이 아닌 일반 로그인 또는 회원가입을 통해 생성되는 객체
 * 사용자에게 토큰이 있을 경우 토큰 정보를 바탕으로 Authentication 객체 생성
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
}
