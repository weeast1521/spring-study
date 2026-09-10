package test.security.JWTSecurity.global.jwt.dto;

import lombok.Builder;

@Builder
public record JwtDTO(
        // String grantType =>  jwt 방식은 그냥 다 Bearer이여서 생략
        String accessToken,
        String refreshToken
) {}
