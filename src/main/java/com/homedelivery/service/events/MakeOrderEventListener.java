package com.homedelivery.service.events;

import com.homedelivery.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MakeOrderEventListener {

    private final EmailService emailService;

    public MakeOrderEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleMakeOrderEvent(MakeOrderEvent makeOrderEvent) {

        this.emailService.sendMakeOrderEmail(makeOrderEvent.getEmail(), makeOrderEvent.getFullName(),
                makeOrderEvent.getOrderId(), makeOrderEvent.getTotalPrice(), makeOrderEvent.getDeliveryAddress(),
                makeOrderEvent.getPhoneNumber());
    }
}