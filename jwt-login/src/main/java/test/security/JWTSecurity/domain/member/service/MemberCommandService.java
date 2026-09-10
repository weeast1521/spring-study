package test.security.JWTSecurity.domain.member.service;

import test.security.JWTSecurity.domain.member.dto.MemberRequestDTO;
import test.security.JWTSecurity.domain.member.dto.MemberResponseDTO;
import test.security.JWTSecurity.global.jwt.dto.JwtDTO;

public interface MemberCommandService {

    MemberResponseDTO.SignUp signUp(MemberRequestDTO.SignUp reqDTO);
    JwtDTO login(MemberRequestDTO.login reqDTO);
}
