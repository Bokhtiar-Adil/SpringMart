package dev.mrb.commercial.security;

import dev.mrb.commercial.exceptions.EmailNotFoundException;
import dev.mrb.commercial.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRegistrationDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .map(AccountRegistrationDetails::new)
                .orElseThrow(()-> new EmailNotFoundException("Account not found"));
    }
}
