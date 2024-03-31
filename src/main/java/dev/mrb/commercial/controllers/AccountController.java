package dev.mrb.commercial.controllers;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.model.dtos.AccountDto;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.EmployeeDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.records.AccountRegistrationRequest;
import dev.mrb.commercial.repositories.VerificationTokenRepository;
import dev.mrb.commercial.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping(path = "/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;

    @PostMapping(path = "/register/employee")
    public ResponseEntity<String> registerEmployee(@RequestBody EmployeeDto employeeDto, final HttpServletRequest request) {
        AccountEntity account = accountService.registerEmployeeAccount(employeeDto);
        publisher.publishEvent(new AccountVerificationEvent(account, buildApplicationUrl(request)));
        return new ResponseEntity<>("Check your mail to confirm account registration", HttpStatus.CREATED);
    }

    @PostMapping(path = "/register/customer")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerDto customerDto, final HttpServletRequest request) {
        AccountEntity account = accountService.registerCustomerAccount(customerDto);
        publisher.publishEvent(new AccountVerificationEvent(account, buildApplicationUrl(request)));
        return new ResponseEntity<>("Check your mail to confirm account registration", HttpStatus.CREATED);
    }
//    @GetMapping("/verifyEmail")
//    public String verifyEmail(@RequestParam("token") String token) {
//        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
//        if (verificationToken.getAccount().isEnabled()) {
//            return "This account has already been verified";
//        }
//        String verificationResult = accountService.validateToken(token);
//        if (verificationResult.equalsIgnoreCase("Valid and enabled")) {
//            return "Email verified successfully";
//        }
//        return "Invalid verification token";
//    }

//    @GetMapping(path = "/accounts")
//    public ResponseEntity<List<AccountDto>> getAllAccounts() {
//        return new ResponseEntity<>(accountService.getAllEmployees(), HttpStatus.FOUND);
//    }

    public String buildApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
