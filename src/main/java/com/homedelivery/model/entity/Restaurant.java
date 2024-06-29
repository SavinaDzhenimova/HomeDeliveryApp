package com.homedelivery.model.entity;

import com.homedelivery.model.enums.RestaurantName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RestaurantName name;

    @Column(nullable = false)
    @Size(min = 3, max = 200)
    private String description;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min = 3, max = 40)
    private String address;

    @Column(name = "open_time", nullable = false)
    private LocalTime open;

    @Column(name = "close_time", nullable = false)
    private LocalTime close;

    @OneToMany(mappedBy = "restaurant")
    private List<Dish> offeredDishes;

    public Restaurant() {
        this.offeredDishes = new ArrayList<>();
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

    public List<Dish> getOfferedDishes() {
        return offeredDishes;
    }

    public void setOfferedDishes(List<Dish> offeredDishes) {
        this.offeredDishes = offeredDishes;
    }
}