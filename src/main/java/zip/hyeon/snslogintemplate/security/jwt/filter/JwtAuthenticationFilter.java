package zip.hyeon.snslogintemplate.security.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zip.hyeon.snslogintemplate.domain.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.domain.refreshToken.entity.RefreshTokenEntity;
import zip.hyeon.snslogintemplate.domain.refreshToken.service.RefreshTokenService;
import zip.hyeon.snslogintemplate.domain.user.entity.UserRole;
import zip.hyeon.snslogintemplate.domain.user.service.UserService;
import zip.hyeon.snslogintemplate.security.jwt.JwtValidator;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;
import zip.hyeon.snslogintemplate.security.jwt.provider.JwtProvider;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderRequestDTO;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;
import zip.hyeon.snslogintemplate.security.utils.CookieUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URI = "/login/";
    private static final String AUTHENTICATION_CODE_URI = "/login/code/";

    private final JwtValidator jwtValidator;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return shouldNotFilter(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        JwtProviderResponseDTO jwtProviderResponseDTO = CookieUtils.getAccessTokenAndRefreshToken(request);

        String accessToken = jwtProviderResponseDTO.getAccessToken();
        String refreshToken = jwtProviderResponseDTO.getRefreshToken();

        try {
            processJwtAuthentication(accessToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료 로직 동작");
            processReIssueJwt(e.getClaims(), refreshToken, response);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw new AuthenticationServiceException("토큰 검증 단계 오류 발생 : " + e.getMessage());
        }
    }

    private void processReIssueJwt(Claims claims, String refreshToken, HttpServletResponse response) {
        Long userId = claims.get("userId", Long.class);
        String userRole = claims.get("userRole", String.class);

        RefreshTokenEntity refreshTokenEntityFromDB = refreshTokenService.findRefreshToken(userId);

        String refreshTokenFromDB = refreshTokenEntityFromDB.getRefreshToken();

        jwtValidator.getClaims(refreshTokenFromDB);

        boolean isRefreshTokenEquals = jwtValidator.validateRefreshToken(refreshToken, refreshTokenFromDB);
        log.info("토큰 검증 결과 = {}", isRefreshTokenEquals);

        if (isRefreshTokenEquals) {
            JwtProviderRequestDTO jwtProviderRequestDTO = JwtProviderRequestDTO.of(userId, UserRole.valueOf(userRole));
            JwtProviderResponseDTO jwtProviderResponseDTO = jwtProvider.generateAccessTokenAndRefreshToken(jwtProviderRequestDTO);

            CookieUtils.setTokenCookie(jwtProviderResponseDTO, response);

            SaveRefreshTokenDTO saveRefreshTokenDTO = SaveRefreshTokenDTO.of(userId, jwtProviderResponseDTO.getRefreshToken());

            refreshTokenService.deleteRefreshToken(userId);
            refreshTokenService.saveRefreshToken(saveRefreshTokenDTO);

            return;
        }

        throw new AuthenticationServiceException("RefreshToken 불일치");
    }

    private void processJwtAuthentication(String accessToken) {
        Claims claims = jwtValidator.getClaims(accessToken);

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
