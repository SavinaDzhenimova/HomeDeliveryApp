package com.homedelivery.service.events;

import com.homedelivery.service.interfaces.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationListener {

    private final EmailService emailService;

    public UserRegistrationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent) {

        this.emailService.sendRegistrationEmail(userRegistrationEvent.getUserEmail(),
                userRegistrationEvent.getUserFullName());
    }
}
