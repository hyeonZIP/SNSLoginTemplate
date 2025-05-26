package zip.hyeon.snslogintemplate.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;
import zip.hyeon.snslogintemplate.exception.GlobalException;
import zip.hyeon.snslogintemplate.exception.ResultCode;
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

    public static Cookie getCookie(String key, HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, key);

        validateCookie(cookie);

        return cookie;
    }

    private static void validateCookie(Cookie cookie){
        if (cookie == null) throw new GlobalException(ResultCode.COOKIE_NOT_FOUND);
    }
}