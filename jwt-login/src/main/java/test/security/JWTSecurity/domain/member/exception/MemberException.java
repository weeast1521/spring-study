package test.security.JWTSecurity.domain.member.exception;

import lombok.Getter;
import test.security.JWTSecurity.global.apiPayload.exception.CustomException;

@Getter
public class MemberException extends CustomException {

    public MemberException(MemberErrorCode errorCode){
        super(errorCode);
    }
}
