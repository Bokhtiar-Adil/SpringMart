package dev.mrb.commercial.mappers.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.entities.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperImpl implements Mapper<CustomerEntity, CustomerDto> {

    private final ModelMapper modelMapper;

    public CustomerMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerDto mapTo(CustomerEntity customerEntity) {
        CustomerDto customerDto;
        
        customerDto = new CustomerDto();
        customerDto.setCustomerId(customerEntity.getCustomerId());
        customerDto.setFirstName(customerEntity.getFirstName());
        customerDto.setLastName(customerEntity.getLastName());
        customerDto.setPhone(customerEntity.getPhone());
        customerDto.setEmail(customerEntity.getEmail());
        customerDto.setAddress(customerEntity.getAddress());
        customerDto.setUsername(null);
        
        return customerDto;
    }

    @Override
    public CustomerEntity mapFrom(CustomerDto customerDto) {
        CustomerEntity customerEntity;

        customerEntity = new CustomerEntity();
        customerEntity.setFirstName(customerDto.getFirstName());
        customerEntity.setLastName(customerDto.getLastName());
        customerEntity.setPhone(customerDto.getPhone());
        customerEntity.setEmail(customerDto.getEmail());
        customerEntity.setAddress(customerDto.getAddress());

        return customerEntity;
    }
}
