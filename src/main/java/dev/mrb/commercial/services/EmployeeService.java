package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.EmployeeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    boolean exists(Long employeeId);

    boolean checkOrderEditPermission(Long employeeId);

    List<EmployeeDto> getAllEmployees();

    String addNewEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployee(Long id);

    List<EmployeeDto> getEmployeesByName(String name);

    String updateEmployeeDataByHimself(EmployeeDto employeeDto);

    String updateEmployeeDataBySuperior(Long superiorId, Long id, EmployeeDto employeeDto);

    String deleteEmployee(Long id);
}
