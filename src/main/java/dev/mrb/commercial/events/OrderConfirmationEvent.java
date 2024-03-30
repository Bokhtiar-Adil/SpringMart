package dev.mrb.commercial.events;

import dev.mrb.commercial.model.dtos.OrderDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderConfirmationEvent extends ApplicationEvent {

    private OrderDto orderDto;
    private String applicationUrl;
    public OrderConfirmationEvent(OrderDto orderDto, String applicationUrl) {
        super(orderDto);
        this.orderDto = orderDto;
        this.applicationUrl = applicationUrl;
    }
}
