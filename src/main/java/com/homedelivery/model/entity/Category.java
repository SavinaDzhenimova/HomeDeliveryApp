package com.homedelivery.model.entity;

import com.homedelivery.model.enums.CategoryName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CategoryName name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 3, max = 150)
    private String description;

    public Category() {
    }

    public CategoryName getName() {
        return name;
    }

    public void setName(CategoryName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
