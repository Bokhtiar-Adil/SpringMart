package dev.mrb.commercial.events;

import dev.mrb.commercial.model.entities.AccountEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AccountVerificationEvent extends ApplicationEvent {
    private AccountEntity account;
    private String applicationUrl; // to verify, click on this link

    public AccountVerificationEvent(AccountEntity account, String applicationUrl) {
        super(account);
        this.account = account;
        this.applicationUrl = applicationUrl;
    }
}
