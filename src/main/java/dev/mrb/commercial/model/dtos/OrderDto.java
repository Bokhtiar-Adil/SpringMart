package dev.mrb.commercial.model.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

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
    private CustomerDto customer;
    private String comments = null;
    private List<ProductDto> products;
    private List<Long> quantities;
    private List<Long> prices;
}
