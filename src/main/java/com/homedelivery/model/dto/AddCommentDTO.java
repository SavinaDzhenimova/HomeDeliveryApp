package com.homedelivery.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class AddCommentDTO {

    @NotNull
    @Size(min = 5, max = 150)
    private String description;

    @NotNull
    @PositiveOrZero
    private int rating;

    public AddCommentDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
