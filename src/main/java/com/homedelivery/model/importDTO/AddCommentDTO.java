package com.homedelivery.model.importDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AddCommentDTO {

    @NotNull
    @Size(min = 5, max = 150, message = "{add_comment_comment_length}")
    private String description;

    @NotNull
    @Positive(message = "{add_comment_rating_not_null}")
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
