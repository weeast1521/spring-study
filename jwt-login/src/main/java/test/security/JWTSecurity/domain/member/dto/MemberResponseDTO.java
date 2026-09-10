package test.security.JWTSecurity.domain.member.dto;

import lombok.Builder;

public class MemberResponseDTO {

    @Builder
    public record SignUp(
            Long id,
            String username
    ) {
    }
}
