package dev.mrb.commercial.controllers;

import dev.mrb.commercial.events.OrderConfirmationEvent;
import dev.mrb.commercial.model.dtos.OrderDto;
import dev.mrb.commercial.model.entities.EmployeeEntity;
import dev.mrb.commercial.model.enums.OrderStatus;
import dev.mrb.commercial.services.CustomerService;
import dev.mrb.commercial.services.EmployeeService;
import dev.mrb.commercial.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
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
        OrderDto orderDto;

        orderDto = orderService.findOrderById(id);
        if (orderDto != null) return new ResponseEntity<>(orderDto, HttpStatus.FOUND);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/{id}/cancel")
    public ResponseEntity cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/{customerId}/edit")
    public ResponseEntity<String> editOrderByCustomers(@PathVariable Long orderId, @PathVariable Long customerId, @RequestBody OrderDto orderDto) {
        String status;

        if (!customerService.exists(customerId))
            return new ResponseEntity<>("Invalid customer id", HttpStatus.BAD_REQUEST);

        status = orderService.editOrder(orderId, orderDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Order updated", HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/{employeeId}/edit")
    public ResponseEntity<String> editOrderByEmployees(@PathVariable Long orderId, @PathVariable Long employeeId, @RequestBody OrderDto orderDto) {
        String status;

        if (!employeeService.exists(employeeId))
            return new ResponseEntity<>("Invalid employee id", HttpStatus.BAD_REQUEST);

        status = orderService.editOrder(orderId, orderDto);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Order updated", HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos;

        orderDtos = orderService.getAllOrders();

        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PutMapping(path = "/update-order-status/{id}")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Long orderId, @RequestBody OrderStatus newStatus) {
        String status;

        status = orderService.updateStatus(orderId, newStatus);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Status updated", HttpStatus.OK);
    }

    @PutMapping(path = "/update-order-status/{id}")
    public ResponseEntity<String> updateOrderDates(@RequestBody Long orderId, @RequestBody Long dateType, @RequestBody LocalDate newDate) {
        String status;

        status = orderService.updateDates(orderId, dateType, newDate);
        if (!status.equalsIgnoreCase("ok"))
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Date updated", HttpStatus.OK);
    }

    public String buildApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }



}
