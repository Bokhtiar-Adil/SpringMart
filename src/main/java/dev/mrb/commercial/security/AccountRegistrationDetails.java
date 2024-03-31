package dev.mrb.commercial.security;

import dev.mrb.commercial.model.entities.AccountEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class AccountRegistrationDetails implements UserDetails {


    private String username;
    private String password;
    private boolean isEnabled; // after email verification, user will be enabled
    private List<GrantedAuthority> authorities;

    public AccountRegistrationDetails(AccountEntity account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.isEnabled = account.isEnabled();
        this.authorities = Arrays.stream(account.getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
