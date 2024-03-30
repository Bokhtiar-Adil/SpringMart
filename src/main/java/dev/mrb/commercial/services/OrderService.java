package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.OrderEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    String addOrderAndGetConfirmationCode(OrderDto orderDto);

    ResponseEntity<OrderDto> confirmNewOrder(String code, OrderDto orderDto);

    OrderDto findOrderById(Long id);

    void cancelOrder(Long id);

    OrderDto editOrderByCustomers(Long id, Long customerId, OrderDto orderDto);

    OrderDto editOrderByEmployees(Long id, Long employeeId, OrderDto orderDto);

    List<OrderDto> getAllOrders();

    void saveOrderConfirmationToken(OrderEntity order, String token);
}
