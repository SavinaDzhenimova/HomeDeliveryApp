package com.homedelivery.model.importDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class AddOrderDTO {

    @NotNull
    @Size(min = 3, max = 100, message = "Delivery address length must be between 3 and 100 characters!")
    private String deliveryAddress;

    @NotNull
    @Size(min = 7, max = 15, message = "Phone number length must be between 7 and 15 characters!")
    private String phoneNumber;

    public AddOrderDTO() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
