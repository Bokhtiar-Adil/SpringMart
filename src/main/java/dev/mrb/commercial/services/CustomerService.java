package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;

import java.util.List;

public interface CustomerService {
    boolean exists(Long customerId);

    String createNewCustomer(CustomerDto customerDto);

    CustomerDto findCustomerById(Long id);

    List<OrderDto> findOrdersByCustomerId(Long id);

    String editCustomerDataById(Long id, CustomerDto orderDto);

    void deleteCustomerProfileById(Long id);

    List<Long> findCustomerIdsByName(String name);

    List<CustomerDto> getAllCustomers();

    void deleteCustomer(Long id);
}
