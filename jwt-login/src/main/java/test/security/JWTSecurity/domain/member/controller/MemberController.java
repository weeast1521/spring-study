package test.security.JWTSecurity.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.security.JWTSecurity.domain.member.dto.MemberRequestDTO;
import test.security.JWTSecurity.domain.member.dto.MemberResponseDTO;
import test.security.JWTSecurity.domain.member.service.MemberCommandService;
import test.security.JWTSecurity.global.apiPayload.CustomResponse;
import test.security.JWTSecurity.global.jwt.dto.JwtDTO;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/sign-up")
    public CustomResponse<MemberResponseDTO.SignUp> signUp(@RequestBody MemberRequestDTO.SignUp reqDTO) {
        MemberResponseDTO.SignUp resDTO = memberCommandService.signUp(reqDTO);

        return CustomResponse.onSuccess(HttpStatus.CREATED, resDTO);
    }

    @PostMapping("/login")
    public CustomResponse<JwtDTO> login(@RequestBody MemberRequestDTO.login reqDTO) {
        JwtDTO resDTO = memberCommandService.login(reqDTO);

        return CustomResponse.onSuccess(resDTO);
    }
}
