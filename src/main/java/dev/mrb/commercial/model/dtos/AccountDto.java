package dev.mrb.commercial.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String password;
    private boolean isEnabled;
    private String roles;
}
