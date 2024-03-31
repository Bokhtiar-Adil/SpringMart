package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.CustomerEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.model.entities.VerificationTokenEntity;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.repositories.VerificationTokenRepository;
import dev.mrb.commercial.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void saveAccountVerificationToken(AccountEntity account, String token) {
        VerificationTokenEntity newToken = new VerificationTokenEntity(token, account);
        verificationTokenRepository.save(newToken);
    }

    @Override
    public AccountEntity registerEmployeeAccount(EmployeeDto employeeDto) {
        AccountEntity newAccount = new AccountEntity();
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeDto.getEmployeeId()).get();
        newAccount.setEmployee(true);
        newAccount.setEmployeeDetails(employeeEntity);
        newAccount.setUsername(employeeDto.getFirstName() + " " + employeeDto.getLastName());
        newAccount.setPassword(passwordEncoder.encode(employeeDto.getPassword())); // need to encode
        newAccount.setEmail(employeeDto.getEmail());
        newAccount.setEnabled(false); // need to enable
        newAccount.setRoles(employeeDto.getRoles());
        AccountEntity savedAccount = accountRepository.save(newAccount);
        return savedAccount;
    }

    @Override
    public AccountEntity registerCustomerAccount(CustomerDto customerDto) {
        AccountEntity newAccount = new AccountEntity();
        CustomerEntity customerEntity = customerRepository.findById(customerDto.getCustomerId()).get();
        newAccount.setEmployee(false);
        newAccount.setCustomerDetails(customerEntity);
        newAccount.setUsername(customerDto.getUsername());
        newAccount.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        newAccount.setEmail(customerDto.getEmail());
        newAccount.setEnabled(false);
        newAccount.setRoles("CUSTOMER");
        accountRepository.save(newAccount);
        AccountEntity savedAccount = accountRepository.save(newAccount);
        return savedAccount;
    }

    @Override
    public Object getAllEmployees() {
        return null;
    }


}
