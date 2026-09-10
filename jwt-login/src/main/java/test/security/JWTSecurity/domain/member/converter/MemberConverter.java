package test.security.JWTSecurity.domain.member.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.security.JWTSecurity.domain.member.dto.MemberRequestDTO;
import test.security.JWTSecurity.domain.member.dto.MemberResponseDTO;
import test.security.JWTSecurity.domain.member.entity.Member;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {

    // Member -> MemberResponseDTO.SignUp
    public static MemberResponseDTO.SignUp toSignUpResponseDTO(Member member) {
        return MemberResponseDTO.SignUp.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();
    }

    // SignUpRequestDTO -> Member
    public static Member toMember(MemberRequestDTO.SignUp reqDTO, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(reqDTO.username())
                .password(passwordEncoder.encode(reqDTO.password()))
                .build();
    }

}
