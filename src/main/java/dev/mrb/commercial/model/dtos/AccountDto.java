package dev.mrb.commercial.model.dtos;

import dev.mrb.commercial.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private Long accountId;
    private boolean isEmployee;
    private EmployeeDto employeeDetails = null;
    private CustomerDto customerDetails = null;
    private String username;
    private String email;
    private boolean isEnabled;
    private Set<Role> roles;
}
