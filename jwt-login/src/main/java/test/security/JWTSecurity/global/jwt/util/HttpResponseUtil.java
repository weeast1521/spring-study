package test.security.JWTSecurity.global.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import test.security.JWTSecurity.global.apiPayload.CustomResponse;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws
            IOException {
        log.info("[*] Success Response");

        // 객체를 json 문자열로 반환 -> string 리턴
        String responseBody = objectMapper.writeValueAsString(CustomResponse.onSuccess(httpStatus,body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        // 응답 바디에 직접 씀
        response.getWriter().write(responseBody);
    }

    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws
            IOException {
        log.info("[*] Failure Response");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        // 객체를 json 으로 직렬화 해서 직접 출력 스트림에 씀 -> void
        // body 객체를 json 문자열로 직렬화 해서 바로 직접 응답 바디에 넣는 것
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
