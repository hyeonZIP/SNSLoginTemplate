package zip.hyeon.snslogintemplate.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import zip.hyeon.snslogintemplate.security.jwt.JwtFacade;
import zip.hyeon.snslogintemplate.security.jwt.TokenResponse;
import zip.hyeon.snslogintemplate.security.userDetails.CustomOAuth2User;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtFacade jwtFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        TokenResponse tokenResponse = jwtFacade.generateTokenByOAuth(customOAuth2User);

        log.info("AccessToken = {}", tokenResponse.getAccessToken());
        log.info("RefreshToken = {}", tokenResponse.getRefreshToken());

        jwtFacade.saveRefreshToken(customOAuth2User.getUserId(), tokenResponse.getRefreshToken());
        //TODO SecurityContextHolder 에 인증객체 저장하기
    }
}
