package dev.mrb.commercial.controllers;

import dev.mrb.commercial.events.OrderConfirmationEvent;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.services.EmployeeService;
import dev.mrb.commercial.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EmployeeService employeeService;
    private final ApplicationEventPublisher publisher;

    @PostMapping(path = "/new-order/add")
    public String addOrderAndGetConfirmationCode(@RequestBody OrderDto orderDto) {
        return orderService.addOrderAndGetConfirmationCode(orderDto);
    }

    @PostMapping(path = "/new-order/confirm")
    public ResponseEntity<String> confirmNewOrder(@RequestBody OrderDto orderDto,
                                                    final HttpServletRequest request) {
        publisher.publishEvent(new OrderConfirmationEvent(orderDto, buildApplicationUrl(request)));
        return new ResponseEntity<>("Check your mail to confirm order", HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/details")
    public ResponseEntity<OrderDto> getOrderDetails(@PathVariable Long id) {
        OrderDto orderDto = orderService.findOrderById(id);
        if (orderDto != null) return new ResponseEntity<>(orderDto, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/{id}/cancel")
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }

    @PatchMapping(path = "/{id}/{customerId}/edit")
    public ResponseEntity<OrderDto> editOrderByCustomers(@PathVariable Long id, @PathVariable Long customerId,
                                                         @RequestBody OrderDto orderDto) {
        OrderDto editedOrderDto = orderService.editOrderByCustomers(id, customerId, orderDto);
        return new ResponseEntity<>(editedOrderDto, HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/{id}/{employeeId}/edit")
    public ResponseEntity<OrderDto> editOrderByEmployees(@PathVariable Long id, @PathVariable Long employeeId,
                                                         @RequestBody OrderDto orderDto) {
        boolean employeeValidity = employeeService.exists(employeeId);
        if (employeeValidity == false) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        employeeValidity = employeeService.checkOrderEditPermission(employeeId);
        if (employeeValidity == false) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        OrderDto editedOrderDto = orderService.editOrderByEmployees(id, employeeId, orderDto);
        if (editedOrderDto == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(editedOrderDto, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders();
        if (!orderDtos.isEmpty()) return new ResponseEntity<>(orderDtos, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public String buildApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }



}
