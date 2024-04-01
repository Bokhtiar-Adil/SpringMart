package dev.mrb.commercial.model.entities;

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
    private LocalDate deadline;
    private LocalDate deliveryDate;
    private LocalDate shippedDate;
    private String status;
    private Long totalAmount;
    private String paymentMethod;
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CustomerEntity customer;
    private String comments = null;
    private List<String> orderEditHistory = new ArrayList<>();
}
