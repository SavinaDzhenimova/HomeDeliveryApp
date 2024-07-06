package com.homedelivery.model.importDTO;

import com.homedelivery.model.entity.Dish;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddOrderDTO {

    @NotNull
    @Size(min = 3, max = 100, message = "Delivery address length must be between 3 and 100 characters!")
    private String deliveryAddress;

    @NotNull
    @Size(min = 7, max = 15, message = "Phone number length must be between 7 and 15 characters!")
    private String phoneNumber;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    @NotNull
    private LocalDateTime orderedOn;

    @NotNull
    private boolean isDelivered;

    @NotNull
    private Long clientId;

    private List<Dish> dishes;

    public AddOrderDTO() {
        this.orderedOn = LocalDateTime.now();
        this.dishes = new ArrayList<>();
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

    public BigDecimal getTotalPrice() {
        return (this.dishes.size() == 0)
                ? BigDecimal.ZERO
                : this.dishes.stream().map(Dish::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
