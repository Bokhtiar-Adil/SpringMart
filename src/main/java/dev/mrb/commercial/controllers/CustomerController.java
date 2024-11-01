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
    public ResponseEntity<String> addNewCustomer(@RequestBody CustomerDto customerDto, final HttpServletRequest request) {
        String status;

        status = customerService.createNewCustomer(customerDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @GetMapping(path = "/profile/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto foundCustomer;

        foundCustomer = customerService.findCustomerById(id);
        if (foundCustomer == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(foundCustomer, HttpStatus.OK);
    }

    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long id) {
        List<OrderDto> foundOrders;

        foundOrders = customerService.findOrdersByCustomerId(id);
        if (foundOrders.isEmpty())
            return new ResponseEntity<>(foundOrders, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(foundOrders, HttpStatus.OK);
    }

    @PatchMapping(path = "/profile/edit/{id}")
    public ResponseEntity<String> editCustomerData(@PathVariable Long id,
                                                        @RequestBody CustomerDto customerDto) {
        String status;

        status = customerService.editCustomerDataById(id, customerDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping(path = "/profile/{id}")
    public void deleteCustomerProfile(@PathVariable Long id) {
        customerService.deleteCustomerProfileById(id);
    }

    @GetMapping(path = "/search/{name}")
    public ResponseEntity<List<Long>> getPossibleCustomerIdsByName(@PathVariable String name) {
        List<Long> ids;

        ids = customerService.findCustomerIdsByName(name);

        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @GetMapping(path = "/search/all")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customerDtos;

        customerDtos = customerService.getAllCustomers();

        return new ResponseEntity<>(customerDtos, HttpStatus.OK);
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
