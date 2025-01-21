package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.events.OrderConfirmationEvent;
import dev.mrb.commercial.model.dtos.AccountDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.model.entities.CustomerEntity;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.model.enums.Role;
import dev.mrb.commercial.registration.CustomerRegistrationRequest;
import dev.mrb.commercial.registration.EmployeeRegistrationRequest;
import dev.mrb.commercial.registration.token.VerificationToken;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.registration.token.VerificationTokenRepository;
import dev.mrb.commercial.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void saveAccountVerificationToken(AccountEntity account, String token) {
        accountRepository.saveVerificationTokenById(account.getAccountId(), token);
    }

    @Override
    public AccountEntity registerEmployeeAccount(EmployeeRegistrationRequest employee) {
        AccountEntity newAccount;
        EmployeeEntity employeeEntity;

        employeeEntity = employeeRepository.findById(employee.employeeId()).get();
        newAccount = new AccountEntity();
        newAccount.setEmployee(true);
        newAccount.setEmployeeDetails(employeeEntity);
        newAccount.setCustomerDetails(null);
        newAccount.setUsername(employee.username());
        newAccount.setPassword(passwordEncoder.encode(employee.password()));
        newAccount.setEmail(employeeEntity.getEmail());
        newAccount.setEnabled(false);
        newAccount.setRoles(employee.roles());
        newAccount = accountRepository.save(newAccount);
        applicationEventPublisher.publishEvent(new AccountVerificationEvent(newAccount, "http://www.springmart.com/"));

        return newAccount;
    }

    @Override
    public AccountEntity registerCustomerAccount(CustomerRegistrationRequest customer) {
        AccountEntity newAccount;
        CustomerEntity customerEntity;

        newAccount = new AccountEntity();
        customerEntity = customerRepository.findById(customer.customerId()).get();
        newAccount.setEmployee(false);
        newAccount.setEmployeeDetails(null);
        newAccount.setCustomerDetails(customerEntity);
        newAccount.setUsername(customer.username());
        newAccount.setPassword(passwordEncoder.encode(customer.password()));
        newAccount.setEmail(customerEntity.getEmail());
        newAccount.setEnabled(false);
        newAccount.setRoles(EnumSet.of(Role.CUSTOMER));

        newAccount = accountRepository.save(newAccount);
        applicationEventPublisher.publishEvent(new AccountVerificationEvent(newAccount, "http://www.springmart.com/"));

        return newAccount;
    }

    @Override
    public String validateToken(String verificationToken) {
        VerificationToken token;
        AccountEntity account;
        Calendar calendar;

        token = verificationTokenRepository.findByToken(verificationToken);
        if (token == null) {
            return "Invalid verification token";
        }

        account = token.getAccount();
        calendar = Calendar.getInstance();
        if (token.getTokenExpirationTime().getTime() <= calendar.getTime().getTime()) {
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }

        account.setEnabled(true);
        accountRepository.save(account);
        return "Valid and enabled";
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<AccountEntity> accountEntities;
        List<AccountDto> accountDtos;

        accountEntities = accountRepository.findAll();
        accountDtos = new ArrayList<AccountDto>();
        for (AccountEntity accountEntity : accountEntities)
        {
            // work here
        }

        return accountDtos;

    }

    @Override
    public List<AccountDto> getAllEmployeeAccounts() {
        // work here
        return null;

    }

    @Override
    public List<AccountDto> getAllCustomerAccounts() {
        // work here
        return null;
    }
}
