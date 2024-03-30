package dev.mrb.commercial.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "verificationTokens")
public class VerificationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String toVerify;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 15;
    @OneToOne
    @JoinColumn(name = "orderDetails")
    private OrderEntity order = null;

    @OneToOne
    @JoinColumn(name = "accountDetails")
    private AccountEntity account = null;

    public VerificationTokenEntity(String token, OrderEntity order) {
        super();
        this.token = token;
        this.order = order;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationTokenEntity(String token, AccountEntity account) {
        super();
        this.token = token;
        this.account = account;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationTokenEntity(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

}
