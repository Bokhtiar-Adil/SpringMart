package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.dtos.ProductDto;
import dev.mrb.commercial.model.entities.OrderEntity;
import dev.mrb.commercial.model.entities.ProductEntity;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {

    private final Mapper<ProductEntity, ProductDto> productMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDto mapTo(OrderEntity orderEntity) {
        OrderDto orderDto;
        ProductDto productDto;
        List<Long> prices;
        List<ProductDto> productDtos;

        orderDto = new OrderDto();
        orderDto.setOrderId(orderEntity.getOrderId());
        orderDto.setOrderDate(orderEntity.getOrderDate());
        orderDto.setDeliveryDate(orderEntity.getDeliveryDate());
        orderDto.setShippedDate(orderEntity.getShippedDate());
        orderDto.setStatus(orderEntity.getStatus());
        orderDto.setTotalAmount(orderEntity.getTotalAmount());
        orderDto.setPaymentMethod(orderEntity.getPaymentMethod());
        orderDto.setPaymentDate(orderEntity.getPaymentDate());
        orderDto.setCustomerId(orderEntity.getCustomer().getCustomerId());
        orderDto.setCustomerName(orderEntity.getCustomer().getFirstName() + " " + orderEntity.getCustomer().getLastName());
        orderDto.setComments(orderEntity.getComments());
        orderDto.setQuantities(orderEntity.getQuantities());

        productDtos = new ArrayList<ProductDto>();
        for (ProductEntity productEntity : orderEntity.getOrderProducts()) {
            productDto = productMapper.mapTo(productEntity);
            productDtos.add(productDto);
        }
        orderDto.setProducts(productDtos);

        prices = new ArrayList<Long>();
        for (ProductEntity productEntity : orderEntity.getOrderProducts())
            prices.add(productEntity.getSellPrice());
        orderDto.setPrices(prices);

        return orderDto;
    }

    @Override
    public OrderEntity mapFrom(OrderDto orderDto) {
        OrderEntity orderEntity;

        orderEntity = new OrderEntity();
        orderEntity.setConfirmationCode(orderDto.getConfirmationCode());
        orderEntity.setOrderDate(orderDto.getOrderDate());
        orderEntity.setDeliveryDate(orderDto.getDeliveryDate());
        orderEntity.setShippedDate(orderDto.getShippedDate());
        orderEntity.setStatus(orderDto.getStatus());
        orderEntity.setTotalAmount(orderDto.getTotalAmount());
        orderEntity.setPaymentMethod(orderDto.getPaymentMethod());
        orderEntity.setPaymentDate(orderDto.getPaymentDate());
        orderEntity.setCustomer(customerRepository.findById(orderDto.getCustomerId()).get());
        orderEntity.setComments(orderDto.getComments());

        for (Long productId : orderDto.getProductIdsOnly())
            orderEntity.getOrderProducts().add(productRepository.findById(productId).get());

        for (Long quantity : orderDto.getQuantities())
            orderEntity.getQuantities().add(quantity);

        return orderEntity;
    }
}
