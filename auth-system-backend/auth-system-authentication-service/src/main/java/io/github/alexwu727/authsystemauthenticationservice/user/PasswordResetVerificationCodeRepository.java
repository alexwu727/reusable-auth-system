package io.github.alexwu727.authsystemauthenticationservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetVerificationCodeRepository extends JpaRepository<PasswordResetVerificationCode, Long> {

    Optional<PasswordResetVerificationCode> findByUser(User user);
    boolean existsByUser(User user);
}
