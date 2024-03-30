package dev.mrb.commercial.controllers;

import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(path = "/register")
    public ResponseEntity<CustomerDto> registerNewCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.createNewCustomer(customerDto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}/profile")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto foundCustomer = customerService.findCustomerById(id);
        if (foundCustomer != null) return new ResponseEntity<>(foundCustomer, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}/orders")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long id) {
        List<OrderDto> foundOrders = customerService.findOrdersByCustomerId(id);
        if (!foundOrders.isEmpty()) return new ResponseEntity<>(foundOrders, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{id}/profile/edit")
    public ResponseEntity<CustomerDto> editCustomerData(@PathVariable Long id,
                                                        @RequestBody CustomerDto customerDto) {
        CustomerDto editedCustomer = customerService.editCustomerDataById(id, customerDto);
        return new ResponseEntity<>(editedCustomer, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{id}/profile")
    public void deleteCustomerProfile(@PathVariable Long id) {
        customerService.deleteCustomerProfileById(id);
    }

    @GetMapping(path = "/{id}/status")
    public ResponseEntity<String> getCustomerStatus(@PathVariable Long id) {
        String status = customerService.findCustomerStatusById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/profile/change-status")
    public ResponseEntity<CustomerDto> updateCustomerStatus(@PathVariable Long id, @RequestBody String status) {
        CustomerDto editedCustomer = customerService.updateCustomerStatusById(id, status);
        return new ResponseEntity<>(editedCustomer, HttpStatus.OK);
    }

    @GetMapping(path = "/search/{name}")
    public ResponseEntity<List<Long>> getPossibleCustomerIdsByName(@PathVariable String name) {
        List<Long> ids = customerService.findCustomerIdsByName(name);
        if (!ids.isEmpty()) return new ResponseEntity<>(ids, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customerDtos = customerService.getAllCustomers();
        if (!customerDtos.isEmpty()) return new ResponseEntity<>(customerDtos, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
