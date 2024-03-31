package dev.mrb.commercial.events.listeners;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.services.AccountService;
import dev.mrb.commercial.services.CustomerService;
import dev.mrb.commercial.services.EmployeeService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountVerificationEventListener implements ApplicationListener<AccountVerificationEvent> {

    private final AccountService accountService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(AccountVerificationEvent event) {
        AccountEntity accountEntity = event.getAccount();
        String verificationToken = UUID.randomUUID().toString();
        accountService.saveAccountVerificationToken(accountEntity, verificationToken);
        String url;
        if (accountEntity.isEmployee()) {
            url = event.getApplicationUrl() + "/employees/add?token=" + verificationToken;
        } else {
            url = event.getApplicationUrl() + "/customers/register?token=" + verificationToken;
        }
        try {
            sendAccountVerificationEmail(accountEntity, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAccountVerificationEmail(AccountEntity account, String url) throws Exception {
        String subject = "Account verification link";
        String senderName = "SpringMart Desk";
        String mailContent = "<p>Thanks for creating a new account in our website. To confirm the account, please " +
                ":<a href=\""+url+"\">click here</a>.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("insertYourServerEmail@gmail.com", senderName);
        messageHelper.setTo(account.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}
