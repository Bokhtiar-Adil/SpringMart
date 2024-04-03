package dev.mrb.commercial.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class AccountRegistrationSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/register/**").permitAll();
                    registry.requestMatchers("/accounts/**").hasRole("ADMIN");
                    registry.requestMatchers("/employees").hasAnyAuthority("ADMIN","MODERATOR","EMPLOYEE");
                    registry.requestMatchers("/customers/profile/edit/**").hasRole("CUSTOMER");
                    registry.requestMatchers("/customers/profile/change-status/**").hasAnyAuthority("ADMIN","MODERATOR");
                    registry.requestMatchers("/customers/search/**").hasAnyAuthority("ADMIN","MODERATOR");
                    registry.requestMatchers("/employees/superior/**").hasRole("ADMIN");
                    registry.requestMatchers("/employees/**").hasAnyAuthority("ADMIN","MODERATOR","EMPLOYEE");
                    registry.requestMatchers("/orders/all").hasAnyAuthority("ADMIN","MODERATOR");
                    registry.requestMatchers("/products/res/**").hasAnyAuthority("ADMIN","MODERATOR");
                    registry.anyRequest().hasAnyAuthority("ADMIN","MODERATOR","EMPLOYEE","CUSTOMER");
                })
                .formLogin((needToWorkHere) -> {})
                .build();
    }

}
