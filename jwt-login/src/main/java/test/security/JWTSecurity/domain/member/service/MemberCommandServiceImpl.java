package test.security.JWTSecurity.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.security.JWTSecurity.domain.member.converter.MemberConverter;
import test.security.JWTSecurity.domain.member.dto.MemberRequestDTO;
import test.security.JWTSecurity.domain.member.dto.MemberResponseDTO;
import test.security.JWTSecurity.domain.member.entity.Member;
import test.security.JWTSecurity.domain.member.exception.MemberErrorCode;
import test.security.JWTSecurity.domain.member.exception.MemberException;
import test.security.JWTSecurity.domain.member.repository.MemberRepository;
import test.security.JWTSecurity.global.jwt.dto.JwtDTO;
import test.security.JWTSecurity.global.jwt.exception.SecurityErrorCode;
import test.security.JWTSecurity.global.jwt.exception.SecurityException;
import test.security.JWTSecurity.global.jwt.service.TokenCommandService;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final TokenCommandService tokenCommandService;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDTO.SignUp signUp(MemberRequestDTO.SignUp reqDTO) {
        Member member = MemberConverter.toMember(reqDTO, passwordEncoder);
        Member savedMember = memberRepository.save(member);

        return MemberConverter.toSignUpResponseDTO(savedMember);
    }

    public JwtDTO login(MemberRequestDTO.login reqDTO) {
        Member member = memberRepository.findByUsername(reqDTO.username())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(reqDTO.password(), member.getPassword())) {
            throw new SecurityException(SecurityErrorCode.BAD_CREDENTIALS);
        }

        return tokenCommandService.createJwtToken(member);
    }
}