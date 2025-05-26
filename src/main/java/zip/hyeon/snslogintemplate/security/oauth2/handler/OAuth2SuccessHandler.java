package zip.hyeon.snslogintemplate.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zip.hyeon.snslogintemplate.domain.auth.entity.Provider;
import zip.hyeon.snslogintemplate.domain.refreshToken.dto.RefreshTokenDTO;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.security.jwt.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.JwtType;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtResponseDTO;
import zip.hyeon.snslogintemplate.security.jwt.facade.JwtFacade;
import zip.hyeon.snslogintemplate.security.oauth2.dto.CustomOAuth2User;
import zip.hyeon.snslogintemplate.security.oauth2.dto.OAuth2UserDTO;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtFacade jwtFacade;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customOAuth2User.getUserId();
        UserRole userRole = customOAuth2User.getUserRole();

        // 1. 토큰 발급
        JwtResponseDTO jwtResponseDTO = jwtFacade.issueToken(JwtRequestDTO.of(userId, userRole));

        log.info("Access Token = {}", jwtResponseDTO.getAccessToken());
        log.info("Refresh Token = {}", jwtResponseDTO.getRefreshToken());

        // 2. RefreshToken 저장
        jwtFacade.saveRefreshToken(RefreshTokenDTO.of(userId, jwtResponseDTO.getRefreshToken()));

        //TODO SecurityContextHolder 에 인증객체 저장하기
    }
}
