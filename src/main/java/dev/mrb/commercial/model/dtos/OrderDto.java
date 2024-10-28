package dev.mrb.commercial.model.dtos;

import dev.mrb.commercial.model.enums.OrderStatus;
import dev.mrb.commercial.model.enums.PaymentMethod;
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
    private LocalDate deliveryDate;
    private LocalDate shippedDate;
    private OrderStatus status;
    private Long totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDate paymentDate;
    private Long customerId;
    private String customerName;
    private String comments = null;
    private List<ProductDto> products;
    private List<Long> quantities;
    private List<Long> prices;
}
