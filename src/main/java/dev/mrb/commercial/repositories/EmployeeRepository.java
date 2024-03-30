package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT * FROM EmployeeEntity WHERE firstName = :name OR lastName = :name")
    List<EmployeeEntity> findEmployeeByName(String name);
}
