package com.homedelivery.service;

import com.homedelivery.model.entity.Category;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository mockCategoryRepository;

    private CategoryServiceImpl categoryServiceToTest;

    @BeforeEach
    public void setUp() {
        this.categoryServiceToTest = new CategoryServiceImpl(mockCategoryRepository);
    }

    @Test
    public void testFindByName() {
        CategoryName categoryName = CategoryName.MAIN_DISH;

        Category category = new Category();
        category.setName(categoryName);

        when(mockCategoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryServiceToTest.findByName(categoryName);

        assertEquals(categoryName, foundCategory.get().getName());
    }

    @Test
    public void testFindByNameNotFound() {

        Category category = new Category();

        when(mockCategoryRepository.findByName(category.getName())).thenReturn(Optional.empty());

        Optional<Category> foundCategory = categoryServiceToTest.findByName(category.getName());

        assertFalse(foundCategory.isPresent());
    }
}