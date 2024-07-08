package com.homedelivery.model.events;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class MakeOrderEvent extends ApplicationEvent {

    private String email;

    private String fullName;

    private Long orderId;

    private BigDecimal totalPrice;

    private String deliveryAddress;

    private String phoneNumber;

    public MakeOrderEvent(Object source,
                          String email,
                          String fullName,
                          Long orderId,
                          BigDecimal totalPrice,
                          String deliveryAddress,
                          String phoneNumber) {
        super(source);
        this.email = email;
        this.fullName = fullName;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}