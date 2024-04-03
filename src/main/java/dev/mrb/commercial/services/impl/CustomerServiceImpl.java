package dev.mrb.commercial.services.impl;

import dev.mrb.commercial.exceptions.CustomerNotFoundException;
import dev.mrb.commercial.mappers.Mapper;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.AccountEntity;
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
    public CustomerDto createNewCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = mapper.mapFrom(customerDto);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
//        AccountEntity newAccount = new AccountEntity();
//        newAccount.setEmployee(false);
//        newAccount.setCustomerDetails(customerEntity);
//        newAccount.setUsername(customerDto.getUsername());
//        newAccount.setPassword(customerDto.getPassword());
//        newAccount.setEmail(customerDto.getEmail());
//        newAccount.setEnabled(false);
//        newAccount.setRoles("CUSTOMER");
//        accountRepository.save(newAccount);
        return mapper.mapTo(savedEntity);
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        if (!customerEntity.isEmpty()) return mapper.mapTo(customerEntity.get());
        else return null;
    }

    @Override
    public List<OrderDto> findOrdersByCustomerId(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        if (customerEntity.isEmpty()) throw new CustomerNotFoundException("Invalid customer id");
        List<Long> orderIds = orderRepository.findOrderIdsByCustomer(customerEntity);
        List<OrderDto> orderDtos = new ArrayList<>();
        if (orderIds.isEmpty()) return orderDtos;
        else {
            for (Long orderId : orderIds) {
                Optional<OrderEntity> order = orderRepository.findById(orderId);
                OrderDto orderDto = mapperOrder.mapTo(order.get());
                orderDtos.add(orderDto);
            }
        }
        return null;
    }

    @Override
    public CustomerDto editCustomerDataById(Long id, CustomerDto customerDto) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        if (customerDto.getCustomerId() != null) customerEntity.get().setCustomerId(customerDto.getCustomerId());
        if (customerDto.getAddress() != null) customerEntity.get().setAddress(customerDto.getAddress());
        if (customerDto.getEmail() != null) customerEntity.get().setEmail(customerDto.getEmail());
        if (customerDto.getPhone() != null) customerEntity.get().setPhone(customerDto.getPhone());
        if (customerDto.getFirstName() != null) customerEntity.get().setFirstName(customerDto.getFirstName());
        if (customerDto.getLastName() != null) customerEntity.get().setLastName(customerDto.getLastName());
        CustomerEntity updatedCustomer = customerRepository.save(customerEntity.get());
        return mapper.mapTo(updatedCustomer);
    }

    @Override
    public void deleteCustomerProfileById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public String findCustomerStatusById(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        return customerEntity.get().getStatus();
    }

    @Override
    public List<Long> findCustomerIdsByName(String name) {
        List<Long> possibleCustomerIds = customerRepository.findIdByName(name);
        return null;
    }

    @Override
    public CustomerDto updateCustomerStatusById(Long id, String status) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        customerEntity.get().setStatus(status);
        return mapper.mapTo(customerEntity.get());
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();
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
