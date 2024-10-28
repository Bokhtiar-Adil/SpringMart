package dev.mrb.commercial.model.entities;

import dev.mrb.commercial.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private boolean isEmployee;

    @OneToOne(fetch = FetchType.LAZY)
    private EmployeeEntity employeeDetails;

    @OneToOne(fetch = FetchType.LAZY)
    private CustomerEntity customerDetails;

    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean isEnabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}