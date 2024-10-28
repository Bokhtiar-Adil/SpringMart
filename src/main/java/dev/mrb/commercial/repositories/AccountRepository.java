package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT roles FROM AccountEntity WHERE employeeDetails = :employee")
    String findRolesByEmployee(EmployeeEntity employee);

    @Query("DELETE FROM AccountEntity WHERE employeeDetails = :toBeDeletedEmployee")
    void deleteByEmployee(EmployeeEntity toBeDeletedEmployee);

    @Query("SELECT a FROM AccountEntity a WHERE a.email = :email")
    Optional<AccountEntity> findByEmail(String email);

    @Query("SELECT a.email FROM AccountEntity a WHERE employeeDetails = :employee")
    String getEmailByEmployee(EmployeeEntity employee);

    @Query("SELECT a.username FROM AccountEntity a WHERE employeeDetails = :employee")
    String getUsernameByEmployee(EmployeeEntity employee);

}
