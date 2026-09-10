package test.security.JWTSecurity.global.jwt.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import test.security.JWTSecurity.domain.member.entity.Member;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.time.Instant;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${spring.jwt.time.access-expiration}") long accessExpiration,
                   @Value("${spring.jwt.time.refresh-expiration}") long refreshExpiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = Duration.ofMillis(accessExpiration);
        this.refreshExpiration = Duration.ofMillis(refreshExpiration);
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명 위조 여부 검증
                .clockSkewSeconds(60)  // 60초 오차 허용 (서버 간 시간 차이 보완)
                .build()
                .parseSignedClaims(token); // 형식, 서명, 만료, Base64 등 모두 검증
    }

    // parser -> JWT 해석기, verifyWith -> 이 secretKey 로 해석해라.
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // 두 에러 중 하나라도 터지면 e로 잡아라!!
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }

    // claim은 key value로 이루어져 있음
    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    //토큰의 유효성 검사
    public boolean isValid(String token) {
        try {
            if (token == null || token.isBlank()) return false;
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public void validateToken(String token) {
        log.info("[ JwtUtil ] 토큰의 유효성을 검증합니다.");

        try {
            // 구문 분석 시스템의 시계가 JWT를 생성한 시스템의 시계 오차 고려
            Jwts.parser()
                    .clockSkewSeconds(60)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // 만료, 위조, 형식 모두 여기서 검사

        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            //원하는 Exception throw
            throw new SecurityException("잘못된 토큰입니다.");
        } catch (ExpiredJwtException e) {
            //원하는 Exception throw
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        }
    }

    // HTTP 요청의 'Authorization' 헤더에서 JWT 액세스 토큰을 추출
    public String resolveAccessToken(HttpServletRequest request) {
        log.info("[ JwtUtil ] 헤더에서 토큰을 추출합니다.");

        String tokenFromHeader = request.getHeader("Authorization");

        if (tokenFromHeader == null || !tokenFromHeader.startsWith("Bearer ")) {
            log.warn("[ JwtUtil ] Request Header 에 토큰이 존재하지 않습니다.");

            return null;
        }

        log.info("[ JwtUtil ] 헤더에 토큰이 존재합니다.");

        return tokenFromHeader.split(" ")[1]; // Bearer 와 분리
    }

    // ======== 토큰 생성 ========
    public String createAccessToken(Member member) {
        return creteToken(member, accessExpiration);
    }

    public String createRefreshToken(Member member) {
        return creteToken(member, refreshExpiration);
    }

    private String creteToken(Member member, Duration ms) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(member.getUsername())
                .claim("id", member.getId())
                .issuedAt(Date.from(now)) // 언제 발급한지
                .expiration(Date.from(now.plus(ms))) // 언제까지 유효한지
                .signWith(secretKey) // 자동으로 적절한 header 생성 및 signature 생성
                .compact();
    }
}
