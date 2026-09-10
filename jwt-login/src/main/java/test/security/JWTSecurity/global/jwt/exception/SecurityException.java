package test.security.JWTSecurity.global.jwt.exception;

import lombok.Getter;
import test.security.JWTSecurity.global.apiPayload.exception.CustomException;

@Getter
public class SecurityException extends CustomException {

    public SecurityException(SecurityErrorCode errorCode){
        super(errorCode);
    }
}
