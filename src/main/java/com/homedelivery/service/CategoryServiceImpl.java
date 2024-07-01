package com.homedelivery.service;

import com.homedelivery.model.entity.Category;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.repository.CategoryRepository;
import com.homedelivery.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findByName(CategoryName name) {
        return this.categoryRepository.findByName(name);
    }
}
