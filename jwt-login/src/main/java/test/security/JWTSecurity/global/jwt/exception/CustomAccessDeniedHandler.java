package test.security.JWTSecurity.global.jwt.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import test.security.JWTSecurity.global.jwt.util.HttpResponseUtil;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    // 로그인은 했지만 권한이 없는 사용자가 접근 -> ROLE_USER 만 있는 사용자가 ROLE_ADMIN 이 필요한 API에 접근

        @Override
        public void handle(HttpServletRequest request,
                           HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException, ServletException {

            SecurityErrorCode errorCode = SecurityErrorCode.FORBIDDEN;

            HttpResponseUtil.setErrorResponse(response, errorCode.getHttpStatus(), errorCode.getErrorResponse());
        }
    }
