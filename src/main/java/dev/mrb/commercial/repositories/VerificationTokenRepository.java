package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
}
