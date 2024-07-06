package com.homedelivery.model.importDTO;

import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.model.enums.RestaurantName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class AddDishDTO {

    @NotNull
    @Size(min = 3, max = 20, message = "Name length must be between 3 and 20 characters!")
    private String name;

    @NotNull
    @Size(min = 3, max = 150, message = "Description length must be between 3 and 150 characters!")
    private String description;

    @NotNull(message = "Price cannot be empty!")
    @Positive(message = "Price cannot be negative number!")
    private BigDecimal price;

    @NotNull
    @Size(min = 3, max = 50, message = "Image URL length must be between 3 and 50 characters!")
    private String imageUrl;

    @NotNull(message = "Category cannot be empty!")
    private CategoryName category;

    @NotNull(message = "Restaurant cannot be empty!")
    private RestaurantName restaurant;

    public AddDishDTO() {
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

    public CategoryName getCategory() {
        return category;
    }

    public void setCategory(CategoryName category) {
        this.category = category;
    }

    public RestaurantName getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantName restaurant) {
        this.restaurant = restaurant;
    }
}
