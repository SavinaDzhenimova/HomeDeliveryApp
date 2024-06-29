package com.homedelivery.init;

import com.homedelivery.model.entity.Category;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CategoriesInit implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoriesInit(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {

        if (this.categoryRepository.count() == 0) {

            Arrays.stream(CategoryName.values())
                    .forEach(categoryName -> {
                        Category category = new Category();
                        category.setName(categoryName);

                        String description = switch (categoryName) {
                            case SALAD -> "Raw greens (such as lettuce) often combined with other vegetables and toppings and served especially with dressing.";
                            case STARTER -> "Starters consist of some interesting varieties of foods that are eaten before the main meal.";
                            case MAIN_DISH -> "The main dish is the primary course of a meal. It typically consists of a protein, accompanied by a starch salad.";
                            case DESSERT -> "Dessert is a course that concludes a meal. The course consists of sweet foods and possibly a beverage.";
                        };

                        category.setDescription(description);
                        this.categoryRepository.saveAndFlush(category);
                    });
        }
    }
}