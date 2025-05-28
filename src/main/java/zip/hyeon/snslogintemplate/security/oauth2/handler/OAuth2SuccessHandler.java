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
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.security.jwt.provider.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.security.jwt.service.JwtService;
import zip.hyeon.snslogintemplate.security.oauth2.dto.CustomOAuth2User;
import zip.hyeon.snslogintemplate.security.utils.CookieUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customOAuth2User.getUserId();
        UserRole userRole = customOAuth2User.getUserRole();

        // 1. 토큰 발급
        JwtProviderRequestDTO jwtProviderRequestDTO = JwtProviderRequestDTO.of(userId, userRole);
        JwtProviderResponseDTO jwtProviderResponseDTO = jwtService.generateAccessTokenAndRefreshToken(jwtProviderRequestDTO);

        log.info("Access Token = {}", jwtProviderResponseDTO.getAccessToken());
        log.info("Refresh Token = {}", jwtProviderResponseDTO.getRefreshToken());

        // 2. RefreshToken 저장, 이미 있다면 덮어씌우기
        SaveRefreshTokenDTO saveRefreshTokenDTO = SaveRefreshTokenDTO.of(userId, jwtProviderResponseDTO.getRefreshToken());
        jwtService.saveRefreshTokenForOAuth2Login(saveRefreshTokenDTO);

        // 3. response 에 Token 을 담은 Cookie 저장
        CookieUtils.setTokenCookie(jwtProviderResponseDTO, response);

        // 4. redirect uri 지정 (임시)
//        response.sendRedirect("http://localhost:8080/login-success");
    }
}
