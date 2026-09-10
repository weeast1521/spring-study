package test.security.JWTSecurity.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.Filter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.security.JWTSecurity.global.jwt.exception.CustomAccessDeniedHandler;
import test.security.JWTSecurity.global.jwt.exception.CustomEntryPoint;
import test.security.JWTSecurity.global.jwt.filter.JwtFilter;
import test.security.JWTSecurity.global.jwt.util.JwtUtil;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final CustomEntryPoint customEntryPoint;
    private final CustomAccessDeniedHandler jwtAccessDeniedHandler;

    // 아래 3개는 Swagger에 대한 URL
    private String[] allowUrl = {
            "/auth/sign-up",
            "/auth/login",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated()
                );
        http
                // 세션 방식에서는 세션이 고정되기 때문에 csrf 공격을 방어해야 하지만, JWT 방식은 stateless 방식이기에 방어하지 않아도 된다.
                .csrf((auth) -> auth.disable())
                // jwt 방식 로그인이기에 form, basic 방식을 모두 disable
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
        ;

        return http.build();
    }

    @Bean
    public Filter jwtFilter() {
        return new JwtFilter(jwtUtil, userDetailsService);
    }

    @Bean
    // AuthenticationProvider에서 사용할 passwordEncoder 설정
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // SecurityContextRepository 빈 등록
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}