package test.security.JWTSecurity.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.security.JWTSecurity.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
