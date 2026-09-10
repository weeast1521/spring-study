package test.security.JWTSecurity.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import test.security.JWTSecurity.global.jwt.exception.SecurityErrorCode;
import test.security.JWTSecurity.global.jwt.util.HttpResponseUtil;
import test.security.JWTSecurity.global.jwt.util.JwtUtil;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        log.info("[ JwtAuthorizationFilter ] 인가 필터 작동");

        try {
            // Request 에서 access token 추출
            String accessToken = jwtUtil.resolveAccessToken(request);

            // accessToken 없이 접근할 경우 필터를 건너뜀
            if (accessToken == null) {
                log.info("[ JwtAuthorizationFilter ] Access Token 이 존재하지 않음. 필터를 건너뜁니다.");
                filterChain.doFilter(request, response);
                return;
            }

            authenticateAccessToken(accessToken);

            log.info("[ JwtAuthorizationFilter ] 종료. 다음 필터로 넘어갑니다.");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("[ JwtAuthorizationFilter ] accessToken 이 만료되었습니다.");
            handleException(response, SecurityErrorCode.TOKEN_EXPIRED);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("[ JwtAuthorizationFilter ] 잘못된 토큰입니다.");
            handleException(response, SecurityErrorCode.INVALID_TOKEN);
        } catch (UsernameNotFoundException e) {
            log.warn("[ JwtAuthorizationFilter ] 사용자 정보를 찾을 수 없습니다.");
            handleException(response, SecurityErrorCode.USER_NOT_FOUND);
        }
    }

    //Access 토큰의 유효성을 검사하는 메서드
    private void authenticateAccessToken(String accessToken) {
        log.info("[ JwtAuthorizationFilter ] 토큰으로 인가 과정을 시작합니다.");

        // AccessToken 유효성 검증
        jwtUtil.validateToken(accessToken);
        log.info("[ JwtAuthorizationFilter ] Access Token 유효성 검증 성공.");

        String username = jwtUtil.getUsername(accessToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.info("[ JwtAuthorizationFilter ] UserDetails 객체 생성 성공");

        // Spring Security 인증 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // 이미 인증이 완료된 사용자를 SecurityContextHolder 에 넣을 때의 패턴
                userDetails.getAuthorities());

        // JWT 기반의 토큰 인증에서는 세션을 사용하지 않기 때문에, SecurityContextHolder 에 현재 인증 객체 저장
        // 다음 요청이 들어올 때마다 새로운 JwtAuthorizationFilter 가 작동하여 JWT 토큰을 검증하고,
        // 그 때마다 SecurityContextHolder 에 인증 정보를 설정하는 방식으로 동작
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("[ JwtAuthorizationFilter ] 인증 객체 저장 완료");
    }

    // 예외 발생 시 HttpResponseUtil 을 사용하여 에러 응답을 처리하는 메서드
    private void handleException(HttpServletResponse response, SecurityErrorCode errorCode) throws IOException {
        // HttpResponseUtil 을 사용하여 에러 응답을 처리
        HttpResponseUtil.setErrorResponse(response, errorCode.getHttpStatus(), errorCode.getErrorResponse());
    }
}
