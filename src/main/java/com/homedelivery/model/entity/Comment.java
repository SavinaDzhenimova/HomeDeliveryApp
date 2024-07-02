package com.homedelivery.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 5, max = 150)
    private String description;

    @Column(nullable = false)
    @PositiveOrZero
    private int rating;

    @Column(name = "added_on", nullable = false)
    private LocalDateTime addedOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "added_by_id", referencedColumnName = "id")
    private User addedBy;

    public Comment() {
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

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public User getUser() {
        return addedBy;
    }

    public void setUser(User addedBy) {
        this.addedBy = addedBy;
    }

    public String parseDateToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return date.format(formatter);
    }
}
