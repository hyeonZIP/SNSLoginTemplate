package zip.hyeon.snslogintemplate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {
    // 글로벌 1000번대
    SUCCESS(HttpStatus.OK, 0, "정상 처리 되었습니다."),

    // 인증, 인가
    INVALID_REGISTRATION_ID(HttpStatus.NOT_FOUND, 1000, "유효하지 않은 SNS 로그인"),
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