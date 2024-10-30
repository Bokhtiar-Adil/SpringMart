package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.OrderEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    String addOrderAndGetConfirmationCode(OrderDto orderDto);

    public String confirmNewOrder(String code, Long orderId);

    OrderDto findOrderById(Long id);

    void cancelOrder(Long id);

    String editOrder(Long orderId, OrderDto orderDto);

    List<OrderDto> getAllOrders();

    void saveOrderConfirmationToken(OrderEntity order, String token);
}
