package dev.mrb.commercial.model.entities;

import dev.mrb.commercial.model.enums.OrderStatus;
import dev.mrb.commercial.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String confirmationCode;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private LocalDate shippedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate paymentDate;
    private boolean paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CustomerEntity customer;

    @Column(nullable = true)
    private String comments;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> orderProducts = new ArrayList<ProductEntity>();

    @Builder.Default
    private List<Long> quantities = new ArrayList<Long>();

    private String orderConfirmationToken;
}
