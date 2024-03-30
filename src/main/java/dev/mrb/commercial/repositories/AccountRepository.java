package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT roles FROM AccountEntity WHERE employeeDetails = :employee")
    String findRolesByEmployee(EmployeeEntity employee);

    @Query("DELETE FROM AccountEntity WHERE employeeDetails = :toBeDeletedEmployee")
    void deleteByEmployee(EmployeeEntity toBeDeletedEmployee);
}
