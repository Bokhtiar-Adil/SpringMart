package dev.mrb.commercial.services;

import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createNewCustomer(CustomerDto customerDto);

    CustomerDto findCustomerById(Long id);

    List<OrderDto> findOrdersByCustomerId(Long id);

    CustomerDto editCustomerDataById(Long id, CustomerDto orderDto);

    void deleteCustomerProfileById(Long id);

    String findCustomerStatusById(Long id);

    List<Long> findCustomerIdsByName(String name);

    CustomerDto updateCustomerStatusById(Long id, String status);

    List<CustomerDto> getAllCustomers();

    void deleteCustomer(Long id);
}
