package dev.mrb.commercial.events.listeners;

import dev.mrb.commercial.events.OrderConfirmationEvent;
import dev.mrb.commercial.model.entities.OrderEntity;
import dev.mrb.commercial.repositories.CustomerRepository;
import dev.mrb.commercial.repositories.OrderRepository;
import dev.mrb.commercial.services.OrderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderConfirmationEventListener implements ApplicationListener<OrderConfirmationEvent> {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final JavaMailSender mailSender;
    @Override
    public void onApplicationEvent(OrderConfirmationEvent event) {
        OrderEntity order = orderRepository.findById(event.getOrderDto().getOrderId()).get();
        String customerEmail = customerRepository.findById(event.getOrderDto().getCustomer().getCustomerId()).get().getEmail();
        String verificationToken = UUID.randomUUID().toString();
        orderService.saveOrderConfirmationToken(order, verificationToken);
        String url = event.getApplicationUrl() + "/orders/new-order/confirm?token=" + verificationToken;
        try {
            sendOrderConfirmationEmail(order, url, customerEmail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOrderConfirmationEmail(OrderEntity order, String url, String customerEmail) throws Exception {
        String subject = "Order confirmation link";
        String senderName = "SpringMart Desk";
        String mailContent = "<p>Thanks for ordering from us. To confirm the order, please " +
                ":<a href=\""+url+"\">click here</a>.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("insertYourServerEmail@gmail.com", senderName);
        messageHelper.setTo(customerEmail);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
