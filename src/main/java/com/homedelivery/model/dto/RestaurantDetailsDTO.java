package com.homedelivery.model.dto;

import com.homedelivery.model.enums.RestaurantName;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public class RestaurantDetailsDTO {

    private RestaurantName name;

    private String description;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalTime open;

    private LocalTime close;

    public RestaurantDetailsDTO() {
    }

    public RestaurantName getName() {
        return name;
    }

    public void setName(RestaurantName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }
}
