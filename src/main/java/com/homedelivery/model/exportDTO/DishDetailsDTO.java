package com.homedelivery.model.exportDTO;

import com.homedelivery.model.enums.RestaurantName;

import java.math.BigDecimal;

public class DishDetailsDTO {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private RestaurantName restaurant;

    public DishDetailsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RestaurantName getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantName restaurant) {
        this.restaurant = restaurant;
    }
}
