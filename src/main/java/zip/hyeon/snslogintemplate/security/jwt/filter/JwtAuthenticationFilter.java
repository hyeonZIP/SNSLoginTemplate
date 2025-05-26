package zip.hyeon.snslogintemplate.security.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zip.hyeon.snslogintemplate.security.jwt.JwtType;
import zip.hyeon.snslogintemplate.security.jwt.JwtValidator;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtDTO;
import zip.hyeon.snslogintemplate.security.utils.CookieUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URI = "/login/";
    private static final String AUTHENTICATION_CODE_URI = "/login/code/";

    private final JwtValidator jwtValidator;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return shouldNotFilter(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = CookieUtils.getCookie(JwtType.ACCESS_TOKEN.getCategory(), request).getValue();
        String refreshToken = CookieUtils.getCookie(JwtType.REFRESH_TOKEN.getCategory(), request).getValue();

        JwtDTO jwtDTO = JwtDTO.of(accessToken, refreshToken);

        v1(jwtDTO);
    }

    /**
     * AccessToken 을 검증한다.
     * 만료됐을 경우 RefreshToken 을 가져온다.
     * RefreshToken 이 만료되지 않았을 경우 새로운 AT와 RT 생성
     */
    private void v1(JwtDTO jwtDTO) {
        jwtValidator.validateAccessToken(jwtDTO.getAccessToken());

        Claims claims;

        try {
            claims = jwtValidator.getClaims(jwtDTO.getAccessToken());
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        Long userId = claims.get("userId", Long.class);
        String userRole = claims.get("userRole", String.class);

        log.info("userId = {}", userId);
        log.info("userRole = {}", userRole);

        CustomUserDetails customUserDetails = new CustomUserDetails(userId, userRole);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 토큰 인증 필터를 거치지 않는 요청
     */
    private boolean shouldNotFilter(String uri) {
        return uri.startsWith(AUTHENTICATION_CODE_URI) || uri.startsWith(LOGIN_URI);
    }
}
