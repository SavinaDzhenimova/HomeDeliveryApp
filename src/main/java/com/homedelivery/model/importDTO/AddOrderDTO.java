package com.homedelivery.model.importDTO;

import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddOrderDTO {

    @NotNull
    @Size(min = 3, max = 40)
    private String deliveryAddress;

    @NotNull
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private boolean isDelivered;

    @NotNull
    private User client;

    private List<Dish> dishes;

    public AddOrderDTO() {
        this.date = LocalDateTime.now();
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
