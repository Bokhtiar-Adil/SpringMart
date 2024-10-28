package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.OfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OfficeRepository extends JpaRepository<OfficeEntity, Long> {

    OfficeEntity find();
}
