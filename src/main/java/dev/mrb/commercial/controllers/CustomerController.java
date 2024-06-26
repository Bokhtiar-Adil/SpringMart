package dev.mrb.commercial.controllers;

import dev.mrb.commercial.events.AccountVerificationEvent;
import dev.mrb.commercial.model.dtos.CustomerDto;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.AccountEntity;
import dev.mrb.commercial.services.AccountService;
import dev.mrb.commercial.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final ApplicationEventPublisher publisher;

    @PostMapping(path = "/add")
    public ResponseEntity<CustomerDto> addNewCustomer(@RequestBody CustomerDto customerDto, final HttpServletRequest request) {
        CustomerDto savedCustomer = customerService.createNewCustomer(customerDto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping(path = "/profile/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto foundCustomer = customerService.findCustomerById(id);
        if (foundCustomer != null) return new ResponseEntity<>(foundCustomer, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long id) {
        List<OrderDto> foundOrders = customerService.findOrdersByCustomerId(id);
        if (!foundOrders.isEmpty()) return new ResponseEntity<>(foundOrders, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/profile/edit/{id}")
    public ResponseEntity<CustomerDto> editCustomerData(@PathVariable Long id,
                                                        @RequestBody CustomerDto customerDto) {
        CustomerDto editedCustomer = customerService.editCustomerDataById(id, customerDto);
        return new ResponseEntity<>(editedCustomer, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/profile/{id}")
    public void deleteCustomerProfile(@PathVariable Long id) {
        customerService.deleteCustomerProfileById(id);
    }

    @GetMapping(path = "/{id}/status")
    public ResponseEntity<String> getCustomerStatus(@PathVariable Long id) {
        String status = customerService.findCustomerStatusById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PatchMapping(path = "/profile/change-status/{id}")
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

    @GetMapping(path = "/search/all")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customerDtos = customerService.getAllCustomers();
        if (!customerDtos.isEmpty()) return new ResponseEntity<>(customerDtos, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    public String buildApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
