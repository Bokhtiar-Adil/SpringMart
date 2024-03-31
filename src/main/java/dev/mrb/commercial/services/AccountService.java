package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.records.AccountRegistrationRequest;

public interface AccountService {
    void saveAccountVerificationToken(AccountEntity account, String token);

    AccountEntity registerEmployeeAccount(EmployeeDto employeeDto);

    AccountEntity registerCustomerAccount(CustomerDto customerDto);

    Object getAllEmployees();
}
