package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.model.entities.*;
import dev.mrb.commercial.model.enums.OrderStatus;
import dev.mrb.commercial.model.enums.PaymentMethod;
import dev.mrb.commercial.repositories.*;
import dev.mrb.commercial.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final Mapper<OrderEntity, OrderDto> orderMapper;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    @Override
    public String addOrderAndGetConfirmationCode(OrderDto orderDto) {
        String confirmationCode;
        OrderEntity orderEntity;

        confirmationCode = "aRandomConfirmationCodeDerivedByAnySuitableAlgorithm";
        orderDto.setConfirmationCode(confirmationCode);

        orderEntity = new OrderEntity();
        if (orderDto.getOrderDate() != null)
            orderEntity.setOrderDate(orderDto.getOrderDate());
        if (orderDto.getDeliveryDate() != null)
            orderEntity.setDeliveryDate(orderDto.getDeliveryDate());
        if (orderDto.getShippedDate() != null)
            orderEntity.setShippedDate(orderDto.getShippedDate());
        if (orderDto.getComments() != null)
            orderEntity.setComments(orderDto.getComments());
        if (orderDto.getConfirmationCode() != null)
            orderEntity.setConfirmationCode(orderDto.getConfirmationCode());
        if (orderDto.getTotalAmount() != null)
            orderEntity.setTotalAmount(orderDto.getTotalAmount());
        if (orderDto.getPaymentDate() != null)
            orderEntity.setPaymentDate(orderDto.getPaymentDate());
        if (orderDto.getPaymentMethod() != null)
            orderEntity.setPaymentMethod(orderDto.getPaymentMethod());
        orderEntity.setStatus(OrderStatus.PENDING);
        orderRepository.save(orderEntity);

        return confirmationCode;
    }

    @Override
    public String confirmNewOrder(String code, Long orderId) {
        Optional<OrderEntity> savedOrderEntity;

        savedOrderEntity = orderRepository.findById(orderId);
        if (savedOrderEntity.isEmpty())
            return "Invalid order id";
        if (!code.equals(savedOrderEntity.get().getConfirmationCode()))
            return "Wrong confirmation code";

        savedOrderEntity.get().setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(savedOrderEntity.get());

        return "ok";
    }

    @Override
    public OrderDto findOrderById(Long id) {
        Optional<OrderEntity> orderEntity;

        orderEntity = orderRepository.findById(id);
        if (orderEntity.isEmpty())
            return null;

        return orderMapper.mapTo(orderEntity.get());
    }

    @Override
    public void cancelOrder(Long id) {
        Optional<OrderEntity> orderEntity;

        orderEntity = orderRepository.findById(id);
        if (!orderEntity.isEmpty())
            orderRepository.deleteById(id);

        return;
    }

    @Override
    public String editOrder(Long orderId, OrderDto orderDto) {
        Optional<OrderEntity> orderEntity;
        int i = 0;
        int len = 0;
        Long quantity;
        Long sum;
        Optional<ProductEntity> product;
        List<ProductEntity> updatedProducts;
        List<Long> updatedQuantities;

        orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty())
            return "Invalid order id";

        orderEntity.get().setOrderProducts(new ArrayList<ProductEntity>());
        orderEntity.get().setQuantities(new ArrayList<Long>());
        updatedProducts = new ArrayList<ProductEntity>();
        updatedQuantities = new ArrayList<Long>();
        len = orderDto.getProductIdsOnly().size();
        sum = 0L;

        for (i = 0; i < len; i++)
        {
            product = productRepository.findById(orderDto.getProductIdsOnly().get(i));
            quantity = orderDto.getQuantities().get(i);
            if (product.isEmpty() || quantity <= 0)
                continue;

            updatedProducts.add(product.get());
            updatedQuantities.add(quantity);
            sum += product.get().getSellPrice();
        }

        orderEntity.get().setOrderProducts(updatedProducts);
        orderEntity.get().setQuantities(updatedQuantities);
        orderEntity.get().setTotalAmount(sum);
        orderEntity.get().setOrderDate(LocalDate.now());
        orderEntity.get().setStatus(OrderStatus.IN_PROCESS);

        if (orderDto.getPaymentMethod() != null)
        {
            orderEntity.get().setPaymentMethod(orderDto.getPaymentMethod());
            if (orderDto.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY)
                orderEntity.get().setPaymentStatus(false);
        }

        orderRepository.save(orderEntity.get());

        return "ok";
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderEntity> orders;
        List<OrderDto> orderDtos;

        orders = orderRepository.findAll();
        orderDtos = new ArrayList<OrderDto>();

        for (OrderEntity order : orders) {
            orderDtos.add(orderMapper.mapTo(order));
        }

        return orderDtos;
    }

    @Override
    public String updateStatus(Long orderId, OrderStatus newStatus) {
        Optional<OrderEntity> order;

        order = orderRepository.findById(orderId);
        if (order.isEmpty())
            return "Invalid order id";

        order.get().setStatus(newStatus);
        orderRepository.save(order.get());

        return "ok";
    }

    @Override
    public String updateDates(Long orderId, Long type, LocalDate date)
    {
        Optional<OrderEntity> orderEntity;

        orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty())
            return "Invalid order id";

        if (type == 1) { // shipped date
            orderEntity.get().setShippedDate(date);
        }
        else if (type == 2) { // delivery date
            orderEntity.get().setDeliveryDate(date);
        }
        else
            ;

        orderRepository.save(orderEntity.get());

        return "ok";
    }

    @Override
    public void saveOrderConfirmationToken(OrderEntity order, String token) {
        VerificationTokenEntity newToken = new VerificationTokenEntity(token, order);
        verificationTokenRepository.save(newToken);
    }
}
