package test.security.JWTSecurity.global.jwt.service;

import test.security.JWTSecurity.domain.member.entity.Member;
import test.security.JWTSecurity.global.jwt.dto.JwtDTO;

public interface TokenCommandService {
    JwtDTO createJwtToken(Member member);
}
