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
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

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
    public String editOrderByCustomers(Long id, Long customerId, OrderDto orderDto) {
        Optional<CustomerEntity> customer;
        Optional<OrderEntity> orderEntity;
        int i;
        int len;
        Long quantity;
        Long sum;
        Optional<ProductEntity> product;
        List<ProductEntity> updatedProducts;
        List<Long> updatedQuantities;

        customer = customerRepository.findById(customerId);
        if (customer.isEmpty())
            return "Invalid customer id";

        orderEntity = orderRepository.findById(orderDto.getOrderId());
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

        // working here
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
