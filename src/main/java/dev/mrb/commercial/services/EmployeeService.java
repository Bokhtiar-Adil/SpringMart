package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.EmployeeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    boolean exists(Long employeeId);

    boolean checkOrderEditPermission(Long employeeId);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto addNewEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployee(Long id);

    List<EmployeeDto> getEmployeeByName(String name);

    EmployeeDto updateEmployeeDataByHimself(Long id, EmployeeDto employeeDto);

    EmployeeDto updateEmployeeDataBySuperior(Long superiorId, Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);
}
