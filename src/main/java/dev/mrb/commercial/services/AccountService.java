package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.AccountDto;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.records.AccountRegistrationRequest;
import dev.mrb.commercial.registration.CustomerRegistrationRequest;
import dev.mrb.commercial.registration.EmployeeRegistrationRequest;

import java.util.List;

public interface AccountService {
    void saveAccountVerificationToken(AccountEntity account, String token);

    AccountEntity registerEmployeeAccount(EmployeeRegistrationRequest employee);

    AccountEntity registerCustomerAccount(CustomerRegistrationRequest customer);

    String validateToken(String verificationToken);

    List<AccountDto> getAllAccounts();
    List<AccountDto> getAllEmployeeAccounts();
    List<AccountDto> getAllCustomerAccounts();
}
