package zip.hyeon.snslogintemplate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {
    // 글로벌 1000번대
    SUCCESS(HttpStatus.OK, 0, "정상 처리 되었습니다."),

    // 인증, 인가
    INVALID_REGISTRATION_ID(HttpStatus.NOT_FOUND, 1000, "유효하지 않은 SNS 로그인"),

    // 쿠키
    COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND, 2000, "토큰 정보가 담긴 쿠키를 찾을 수 없습니다."),

    // 토큰
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 2001, "AccessToken 이 없습니다."),
    ;
    private final HttpStatus status;
    private final int code;
    private final String message;

    ResultCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}