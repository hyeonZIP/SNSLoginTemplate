package zip.hyeon.snslogintemplate.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.WebUtils;
import zip.hyeon.snslogintemplate.exception.example.GlobalException;
import zip.hyeon.snslogintemplate.exception.example.ResultCode;
import zip.hyeon.snslogintemplate.security.jwt.JwtType;
import zip.hyeon.snslogintemplate.security.jwt.provider.dto.JwtProviderResponseDTO;

@Slf4j
public class CookieUtils {

    public static void setTokenCookie(JwtProviderResponseDTO jwtProviderResponseDTO, HttpServletResponse response) {
        setResponse(JwtType.ACCESS_TOKEN.getCategory(), jwtProviderResponseDTO.getAccessToken(), JwtType.ACCESS_TOKEN.getExpiredTime(), response);
        setResponse(JwtType.REFRESH_TOKEN.getCategory(), jwtProviderResponseDTO.getRefreshToken(), JwtType.REFRESH_TOKEN.getExpiredTime(), response);
    }

    private static void setResponse(String key, String value, int expiredTime, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiredTime);
        response.addCookie(cookie);
    }

    public static JwtProviderResponseDTO getAccessTokenAndRefreshToken(HttpServletRequest request) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, JwtType.ACCESS_TOKEN.getCategory());
        Cookie refreshTokenCookie = WebUtils.getCookie(request, JwtType.REFRESH_TOKEN.getCategory());

        validateCookie(accessTokenCookie, refreshTokenCookie);

        String accessToken = accessTokenCookie.getValue();
        String refreshToken = refreshTokenCookie.getValue();

        log.info("accessTokenCookie = {}", accessTokenCookie.getValue());
        log.info("refreshTokenCookie = {}", refreshTokenCookie.getValue());
        validateTokenIsNull(accessToken, refreshToken);

        return JwtProviderResponseDTO.of(accessToken, refreshToken);
    }

    private static void validateTokenIsNull(String accessToken, String refreshToken) {
        if (accessToken == null) throw new GlobalException(ResultCode.TOKEN_NOT_FOUND);
        if (refreshToken == null) throw new GlobalException(ResultCode.TOKEN_NOT_FOUND);
    }

    private static void validateCookie(Cookie accessTokenCookie, Cookie refreshTokenCookie) {
        if (accessTokenCookie == null) throw new GlobalException(ResultCode.COOKIE_NOT_FOUND);
        if (refreshTokenCookie == null) throw new GlobalException(ResultCode.COOKIE_NOT_FOUND);
    }
}