package zip.hyeon.snslogintemplate.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final ResultCode resultCode;

    public GlobalException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
