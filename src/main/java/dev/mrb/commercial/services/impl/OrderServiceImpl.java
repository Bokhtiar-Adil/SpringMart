package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.model.entities.*;
import dev.mrb.commercial.repositories.EmployeeRepository;
import dev.mrb.commercial.repositories.OrderDetailsRepository;
import dev.mrb.commercial.repositories.OrderRepository;
import dev.mrb.commercial.repositories.VerificationTokenRepository;
import dev.mrb.commercial.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final Mapper<OrderEntity, OrderDto> mapper;
    private final Mapper<ProductEntity, ProductDto> productMapper;
    
    private String confirmationCode = null;
    @Override
    public String addOrderAndGetConfirmationCode(OrderDto orderDto) {
        confirmationCode = "aRandomConfirmationCodeDerivedByAnySuitableAlgorithm";
        orderDto.setConfirmationCode(confirmationCode);
        OrderEntity orderEntity = new OrderEntity();
        if (orderDto.getOrderDate() != null) orderEntity.setOrderDate(orderDto.getOrderDate());
        if (orderDto.getDeadline() != null) orderEntity.setDeadline(orderDto.getDeadline());
        if (orderDto.getDeliveryDate() != null) orderEntity.setDeliveryDate(orderDto.getDeliveryDate());
        if (orderDto.getShippedDate() != null) orderEntity.setShippedDate(orderDto.getShippedDate());
        if (orderDto.getComments() != null) orderEntity.setComments(orderDto.getComments());
        if (orderDto.getConfirmationCode() != null) orderEntity.setConfirmationCode(orderDto.getConfirmationCode());
        if (orderDto.getTotalAmount() != null) orderEntity.setTotalAmount(orderDto.getTotalAmount());
        if (orderDto.getPaymentDate() != null) orderEntity.setPaymentDate(orderDto.getPaymentDate());
        if (orderDto.getPaymentMethod() != null) orderEntity.setPaymentMethod(orderDto.getPaymentMethod());
        orderEntity.setStatus("NOT_CONFIRMED");
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        return confirmationCode;
    }

    @Override
    public ResponseEntity<OrderDto> confirmNewOrder(String code, OrderDto orderDto) {
        Optional<OrderEntity> savedOrderEntity = orderRepository.findById(orderDto.getOrderId());
        if (code != savedOrderEntity.get().getConfirmationCode()) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        savedOrderEntity.get().setStatus("ORDER_PLACED");
        OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
        orderDetailsEntity.setOrderId(savedOrderEntity.get());
        List<ProductEntity> orderedProducts = new ArrayList<>();
        for (ProductDto product : orderDto.getProducts()) {
            orderedProducts.add(productMapper.mapFrom(product));
        }
        orderDetailsEntity.setProducts(orderedProducts);
        orderDetailsEntity.setPrices(orderDto.getPrices());
        orderDetailsEntity.setQuantities(orderDto.getQuantities());
        OrderEntity confirmedOrderEntity = orderRepository.save(savedOrderEntity.get());
        OrderDetailsEntity confirmedOrderDetailsEntity = orderDetailsRepository.save(orderDetailsEntity);
        orderDto.setOrderId(confirmedOrderEntity.getOrderId());
        orderDto.setStatus(confirmedOrderEntity.getStatus());
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Override
    public OrderDto findOrderById(Long id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (!orderEntity.isEmpty()) return mapper.mapTo(orderEntity.get());
        else return null;
    }

    @Override
    public void cancelOrder(Long id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (!orderEntity.isEmpty()) orderRepository.deleteById(id);
    }

    @Override
    public OrderDto editOrderByCustomers(Long id, Long customerId, OrderDto orderDto) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        Long orderDetailsEntityId = orderDetailsRepository.findByOrderId(id);
        Optional<OrderDetailsEntity> orderDetailsEntity = orderDetailsRepository.findById(orderDetailsEntityId);
        if (orderDto.getComments() != null) orderEntity.get().setComments(orderDto.getComments());
        if (orderDto.getPaymentMethod() != null) orderEntity.get().setPaymentMethod(orderDto.getPaymentMethod());
        if (orderDto.getTotalAmount() != null) orderEntity.get().setTotalAmount(orderDto.getTotalAmount());
        List<ProductEntity> orderedProducts = new ArrayList<>();
        for (ProductDto product : orderDto.getProducts()) {
            orderedProducts.add(productMapper.mapFrom(product));
        }
        if (!orderedProducts.isEmpty()) orderDetailsEntity.get().setProducts(orderedProducts);
        if (!orderDto.getPrices().isEmpty()) orderDetailsEntity.get().setPrices(orderDto.getPrices());
        if (!orderDto.getQuantities().isEmpty()) orderDetailsEntity.get().setQuantities(orderDto.getQuantities());

        String editMessage = "Customer " + customerId.toString() + " edited on " + getCurrentDateTime().toString();
        orderEntity.get().getOrderEditHistory().add(editMessage);
        OrderEntity updatedOrder = orderRepository.save(orderEntity.get());
        orderDto.setOrderId(updatedOrder.getOrderId());
        return orderDto;
    }

    @Override
    public OrderDto editOrderByEmployees(Long id, Long employeeId, OrderDto orderDto) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        Long orderDetailsEntityId = orderDetailsRepository.findByOrderId(id);
        Optional<OrderDetailsEntity> orderDetailsEntity = orderDetailsRepository.findById(orderDetailsEntityId);
        if (orderDto.getDeadline() != null) orderEntity.get().setDeadline(orderDto.getDeadline());
        if (orderDto.getDeliveryDate() != null) orderEntity.get().setDeliveryDate(orderDto.getDeliveryDate());
        if (orderDto.getShippedDate() != null) orderEntity.get().setShippedDate(orderDto.getShippedDate());
        if (orderDto.getStatus() != null) orderEntity.get().setStatus(orderDto.getStatus());
        if (orderDto.getComments() != null) orderEntity.get().setComments(orderDto.getComments());
        if (orderDto.getPaymentDate() != null) orderEntity.get().setPaymentDate(orderDto.getPaymentDate());
        if (orderDto.getPaymentMethod() != null) orderEntity.get().setPaymentMethod(orderDto.getPaymentMethod());
        if (orderDto.getTotalAmount() != null) orderEntity.get().setTotalAmount(orderDto.getTotalAmount());
        List<ProductEntity> orderedProducts = new ArrayList<>();
        for (ProductDto product : orderDto.getProducts()) {
            orderedProducts.add(productMapper.mapFrom(product));
        }
        if (!orderedProducts.isEmpty()) orderDetailsEntity.get().setProducts(orderedProducts);
        if (!orderDto.getPrices().isEmpty()) orderDetailsEntity.get().setPrices(orderDto.getPrices());
        if (!orderDto.getQuantities().isEmpty()) orderDetailsEntity.get().setQuantities(orderDto.getQuantities());

        String editMessage = "Employee " + employeeId.toString() + " edited on " + getCurrentDateTime().toString();
        orderEntity.get().getOrderEditHistory().add(editMessage);
        OrderEntity updatedOrder = orderRepository.save(orderEntity.get());
        orderDto.setOrderId(updatedOrder.getOrderId());
        return orderDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (OrderEntity order : orders) {
            orderDtos.add(mapper.mapTo(order));
        }
        return orderDtos;
    }

    private LocalDateTime getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now;
    }

    @Override
    public void saveOrderConfirmationToken(OrderEntity order, String token) {
        VerificationTokenEntity newToken = new VerificationTokenEntity(token, order);
        verificationTokenRepository.save(newToken);
    }
}
