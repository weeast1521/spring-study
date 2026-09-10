package test.security.JWTSecurity.global.apiPayload.exception;

import lombok.Getter;
import test.security.JWTSecurity.global.apiPayload.code.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException{

    private final BaseErrorCode code;

    public CustomException(BaseErrorCode errorCode) {
        this.code = errorCode;
    }
}
