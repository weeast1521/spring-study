package test.security.JWTSecurity.domain.member.dto;

public class MemberRequestDTO {

    public record SignUp(
            String username,
            String password
    ) {
    }

    public record login(
            String username,
            String password
    ){
    }
}
