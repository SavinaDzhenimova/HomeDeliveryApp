package com.homedelivery.service;

import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.model.entity.Category;
import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.repository.DishRepository;
import com.homedelivery.service.interfaces.CategoryService;
import com.homedelivery.service.interfaces.DishService;
import com.homedelivery.service.interfaces.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public DishServiceImpl(DishRepository dishRepository, RestaurantService restaurantService,
                           CategoryService categoryService, ModelMapper modelMapper) {
        this.dishRepository = dishRepository;
        this.restaurantService = restaurantService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addDish(AddDishDTO addDishDTO) {

        if (addDishDTO == null) {
            return false;
        }

        Optional<Restaurant> optionalRestaurant = this.restaurantService.findByName(addDishDTO.getRestaurant());
        Optional<Category> optionalCategory = this.categoryService.findByName(addDishDTO.getCategory());

        if (optionalCategory.isEmpty() || optionalRestaurant.isEmpty()) {
            return false;
        }

        Category category = optionalCategory.get();
        Restaurant restaurant = optionalRestaurant.get();

        Dish dish = this.modelMapper.map(addDishDTO, Dish.class);
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        this.dishRepository.saveAndFlush(dish);
        return true;
    }
}