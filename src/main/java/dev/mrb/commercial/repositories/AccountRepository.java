package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT roles FROM AccountEntity WHERE employeeDetails = :employee")
    String findRolesByEmployee(EmployeeEntity employee);

    @Query("DELETE FROM AccountEntity WHERE employeeDetails = :toBeDeletedEmployee")
    void deleteByEmployee(EmployeeEntity toBeDeletedEmployee);

    @Query("SELECT * FROM AccountEntity WHERE email = :email")
    Optional<AccountEntity> findByEmail(String email);
}
