package test.security.JWTSecurity.global.jwt.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import test.security.JWTSecurity.global.jwt.util.HttpResponseUtil;

import java.io.IOException;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
    // 로그인하지 않은 사용자가 인증이 필요한 요청을 보냄 -> 인증되지 않은 사용자가 보호된 리소스에 접근할 때 발생

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        SecurityErrorCode errorCode = SecurityErrorCode.UNAUTHORIZED;

        HttpResponseUtil.setErrorResponse(response, errorCode.getHttpStatus(), errorCode.getErrorResponse());
    }
}