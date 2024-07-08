package com.homedelivery.service.interfaces;

import java.math.BigDecimal;
import java.util.Map;

public interface EmailService {

    void sendRegistrationEmail(String userEmail, String username);

    void sendMakeOrderEmail(String email, String fullName, Long orderId, BigDecimal totalPrice,
                            String deliveryAddress, String phoneNumber);

}