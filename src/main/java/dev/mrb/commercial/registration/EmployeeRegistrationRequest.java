package dev.mrb.commercial.registration;

import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.enums.Role;

import java.util.Set;

public record EmployeeRegistrationRequest(Long employeeId, String username, String password, Set<Role> roles) {
}
