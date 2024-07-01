package com.homedelivery.service.interfaces;

import com.homedelivery.model.entity.Category;
import com.homedelivery.model.enums.CategoryName;

import java.util.Optional;

public interface CategoryService {
    Optional<Category> findByName(CategoryName name);
}
