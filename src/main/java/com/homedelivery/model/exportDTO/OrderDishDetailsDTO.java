package com.homedelivery.model.exportDTO;

import java.math.BigDecimal;

public class OrderDishDetailsDTO {

    private String imageUrl;

    private String name;

    private BigDecimal price;

    public OrderDishDetailsDTO() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
