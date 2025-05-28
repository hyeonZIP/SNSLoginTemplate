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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zip.hyeon.snslogintemplate.exception.example.GlobalException;
import zip.hyeon.snslogintemplate.exception.example.ResultCode;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDTO;
import zip.hyeon.snslogintemplate.security.jwt.dto.CustomUserDetails;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;
import zip.hyeon.snslogintemplate.security.jwt.refreshToken.dto.SaveRefreshTokenDTO;
import zip.hyeon.snslogintemplate.security.jwt.service.JwtService;
import zip.hyeon.snslogintemplate.security.utils.CookieUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
/**
 * 토큰 인증 필터
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URI = "/login/";
    private static final String AUTHENTICATION_CODE_URI = "/login/code/";

    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return shouldNotFilter(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        JwtProviderResponseDTO clientTokens = CookieUtils.getAccessTokenAndRefreshToken(request);

        try {
            log.info("토큰 인증 필터 동작");

            processJwtAuthentication(clientTokens.getAccessToken());
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {

            log.info("토큰 만료 로직 동작");
            processJwtReIssuance(e.getClaims(), clientTokens.getRefreshToken(), response);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw new GlobalException(ResultCode.INVALID_REGISTRATION_ID);
        }
    }

    /**
     * 토큰 재발급 후 인증 처리
     */
    private void processJwtReIssuance(Claims claims, String clientRefreshToken, HttpServletResponse response) {
        // 1. AT & RT 재발급
        // 1-1. 만료된 AT 에서 userId 를 꺼냄
        // 1-2. userId에 해당하는 RT 조회
        // 1-3. request에 담긴 RT 와 DB의 RT 동등 비교
        // 1-4. 동등한 RT일 경우 재발급 로직 실행
        JwtProviderResponseDTO newTokens = jwtService.reIssueToken(claims, clientRefreshToken);

        // 2. new AT & RT 쿠키에 저장
        CookieUtils.setTokenCookie(newTokens, response);

        // 3. 새로 생성된 AT 에서 사용자 정보 반환
        CustomUserDTO customUserDTO = jwtService.getUserInfoFromAccessToken(newTokens.getAccessToken());
        Long userId = customUserDTO.getUserId();

        // 4. 기존 RT 삭제 및 새 RT 저장
        SaveRefreshTokenDTO saveRefreshTokenDTO = SaveRefreshTokenDTO.of(userId, newTokens.getRefreshToken());
        jwtService.deleteOldAndNewRefreshToken(saveRefreshTokenDTO);

        setAuthentication(customUserDTO);
    }

    /**
     * 토큰 유효성 확인 및 인증 처리
     */
    private void processJwtAuthentication(String accessToken) {
        CustomUserDTO customUserDTO = jwtService.getUserInfoFromAccessToken(accessToken);
        setAuthentication(customUserDTO);
    }

    /**
     * Authentication 객체 생성 및 저장
     */
    private void setAuthentication(CustomUserDTO customUserDTO) {
        log.info("userId = {}", customUserDTO.getUserId());
        log.info("userRole = {}", customUserDTO.getUserRole());

        CustomUserDetails customUserDetails = new CustomUserDetails(customUserDTO);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 토큰 인증 필터를 거치지 않는 요청
     */
    private boolean shouldNotFilter(String uri) {
        return uri.startsWith(AUTHENTICATION_CODE_URI) || uri.startsWith(LOGIN_URI) || uri.startsWith("/test");
    }
}
