package com.homedelivery.model.importDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddOrderDTO {

    @NotNull
    @Size(min = 3, max = 100, message = "{add_order_delivery_address_length}")
    private String deliveryAddress;

    @NotNull
    @Size(min = 7, max = 15, message = "{add_order_phone_number_length}")
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
