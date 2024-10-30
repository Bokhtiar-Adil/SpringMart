package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.CustomerEntity;
import dev.mrb.commercial.model.entities.OrderEntity;
import dev.mrb.commercial.repositories.AccountRepository;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.OrderRepository;
import dev.mrb.commercial.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final Mapper<CustomerEntity, CustomerDto> mapper;
    private final Mapper<OrderEntity, OrderDto> mapperOrder;

    @Override
    public boolean exists(Long customerId) {
        return customerRepository.existsById(customerId);
    }

    @Override
    public String createNewCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = mapper.mapFrom(customerDto);

        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        if (savedEntity == null)
            return "Failed to save";

        return "ok";
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        Optional<CustomerEntity> customerEntity;

        customerEntity = customerRepository.findById(id);
        if (customerEntity.isEmpty())
            return null;

        return mapper.mapTo(customerEntity.get());
    }

    @Override
    public List<OrderDto> findOrdersByCustomerId(Long id) {
        Optional<CustomerEntity> customerEntity;
        List<Long> orderIds;
        List<OrderDto> orderDtos;

        orderDtos = new ArrayList<>();
        customerEntity = customerRepository.findById(id);
        if (customerEntity.isEmpty())
            return orderDtos;

        orderIds = orderRepository.findOrderIdsByCustomer(customerEntity);
        if (orderIds.isEmpty())
            return orderDtos;

        for (Long orderId : orderIds) {
            Optional<OrderEntity> order = orderRepository.findById(orderId);
            if (order.isEmpty())
                continue;

            OrderDto orderDto = mapperOrder.mapTo(order.get());
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    @Override
    public String editCustomerDataById(Long id, CustomerDto customerDto) {
        Optional<CustomerEntity> customerEntity;

        customerEntity = customerRepository.findById(id);

        if (customerDto.getFirstName() != null)
            customerEntity.get().setFirstName(customerDto.getFirstName());
        if (customerDto.getLastName() != null)
            customerEntity.get().setLastName(customerDto.getLastName());
        if (customerDto.getPhone() != null)
            customerEntity.get().setPhone(customerDto.getPhone());
        if (customerDto.getEmail() != null)
            customerEntity.get().setEmail(customerDto.getEmail());
        if (customerDto.getAddress() != null)
            customerEntity.get().setAddress(customerDto.getAddress());

        CustomerEntity updatedCustomer = customerRepository.save(customerEntity.get());
        if (updatedCustomer == null)
            return "Failed to save";

        return "ok";
    }

    @Override
    public void deleteCustomerProfileById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Long> findCustomerIdsByName(String name) {
        List<Long> possibleCustomerIds = customerRepository.findIdByName(name);
        return possibleCustomerIds;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerEntity> customerEntities;
        List<CustomerDto> customerDtos;

        customerDtos = new ArrayList<>();
        customerEntities = customerRepository.findAll();
        for (CustomerEntity customerEntity : customerEntities) {
            customerDtos.add(mapper.mapTo(customerEntity));
        }

        return customerDtos;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
