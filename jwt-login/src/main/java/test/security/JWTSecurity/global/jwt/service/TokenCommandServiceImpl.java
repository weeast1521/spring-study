package test.security.JWTSecurity.global.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.security.JWTSecurity.domain.member.entity.Member;
import test.security.JWTSecurity.global.jwt.dto.JwtDTO;
import test.security.JWTSecurity.global.jwt.util.JwtUtil;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TokenCommandServiceImpl implements TokenCommandService {

    private final JwtUtil jwtUtil;

    public JwtDTO createJwtToken(Member member) {
        String accessToken = jwtUtil.createAccessToken(member);
        String refreshToken = jwtUtil.createRefreshToken(member);

        return JwtDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
