package zip.hyeon.snslogintemplate.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final Integer statusCode;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public CommonResponse(ResultCode resultCode) {
        this.statusCode = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    // 성공 시 일반적인 생성자
    public CommonResponse(ResultCode resultCode, T data) {
        this.statusCode = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    // 이거 호출로 성공 생성자 자동 호출, 데이터 담아서 반환됨
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResultCode.SUCCESS, data);
    }
}