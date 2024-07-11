package com.homedelivery.model.importDTO;

import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.model.enums.RestaurantName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class AddDishDTO {

    @NotNull
    @Size(min = 3, max = 20, message = "{add_dish_name_length}")
    private String name;

    @NotNull
    @Size(min = 3, max = 150, message = "{add_dish_description_length}")
    private String description;

    @NotNull(message = "{add_dish_price_not_null}")
    @Positive(message = "{add_dish_price_positive}")
    private BigDecimal price;

    @NotNull
    @Size(min = 3, max = 50, message = "{add_dish_image_url_length}")
    private String imageUrl;

    @NotNull(message = "{add_dish_category_not_null}")
    private CategoryName category;

    @NotNull(message = "{add_dish_restaurant_not_null}")
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
