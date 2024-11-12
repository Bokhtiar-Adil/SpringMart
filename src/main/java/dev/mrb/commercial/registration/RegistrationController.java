package dev.mrb.commercial.registration;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.registration.token.*;
import dev.mrb.commercial.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/register")
public class RegistrationController {

    private final AccountService accountService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;

    @PostMapping()
    public String registerEmployeeAccount(@RequestBody EmployeeRegistrationRequest registrationRequest, final HttpServletRequest request) {
        AccountEntity account;

        account = accountService.registerEmployeeAccount(registrationRequest);
        publisher.publishEvent(new AccountVerificationEvent(account, applicationUrl(request)));

        return "Success! Check your mail to confirm registration";
    }

    @PostMapping()
    public String registerCustomerAccount(@RequestBody CustomerRegistrationRequest registrationRequest, final HttpServletRequest request) {
        AccountEntity account;

        account = accountService.registerCustomerAccount(registrationRequest);
        publisher.publishEvent(new AccountVerificationEvent(account, applicationUrl(request)));

        return "Success! Check your mail to confirm registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.getAccount().isEnabled()) {
            return "This account has already been verified";
        }
        String verificationResult = accountService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid and enabled")) {
            return "Email verified successfully";
        }
        return "Invalid verification token";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }



}