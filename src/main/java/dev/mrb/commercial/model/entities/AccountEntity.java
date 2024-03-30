package dev.mrb.commercial.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @OneToOne(fetch = FetchType.EAGER)
    private EmployeeEntity employeeDetails = null;
    @OneToOne(fetch = FetchType.EAGER)
    private CustomerEntity customerDetails = null;
    private String username;
    private String email;
    private String password;
    private boolean isEnabled;
    private String roles;
}
