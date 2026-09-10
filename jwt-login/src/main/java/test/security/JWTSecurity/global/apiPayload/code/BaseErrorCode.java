package test.security.JWTSecurity.global.apiPayload.code;

import org.springframework.http.HttpStatus;
import test.security.JWTSecurity.global.apiPayload.CustomResponse;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();

    default CustomResponse<Void> getErrorResponse() {
        return CustomResponse.onFailure(getCode(), getMessage());
    }
}
