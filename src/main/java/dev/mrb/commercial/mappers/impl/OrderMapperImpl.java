package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements Mapper<OrderEntity, OrderDto> {

    private final ModelMapper modelMapper;

    public OrderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto mapTo(OrderEntity orderEntity) {
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
        return orderDto;
    }

    @Override
    public OrderEntity mapFrom(OrderDto orderDto) {
        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);
        return orderEntity;
    }
}
