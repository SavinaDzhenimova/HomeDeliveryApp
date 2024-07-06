package com.homedelivery.model.importDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AddCommentDTO {

    @NotNull
    @Size(min = 5, max = 150, message = "Comment length must be between 5 and 150 characters!")
    private String description;

    @NotNull
    @Positive(message = "Rating cannot be empty!")
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
