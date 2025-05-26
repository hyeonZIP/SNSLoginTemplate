package zip.hyeon.snslogintemplate.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import zip.hyeon.snslogintemplate.security.jwt.JwtType;
import zip.hyeon.snslogintemplate.security.jwt.dto.JwtDTO;

public class CookieUtils {

    public static void setTokenCookie(JwtDTO jwtDTO, HttpServletResponse response) {
        setResponse(JwtType.ACCESS_TOKEN.getCategory(), jwtDTO.getAccessToken(), JwtType.ACCESS_TOKEN.getExpiredTime(), response);
        setResponse(JwtType.REFRESH_TOKEN.getCategory(), jwtDTO.getRefreshToken(), JwtType.REFRESH_TOKEN.getExpiredTime(), response);
    }

    private static void setResponse(String key, String value, int expiredTime, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiredTime);
        response.addCookie(cookie);
    }
}