package com.homedelivery.service.interfaces;

import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;

import java.util.Optional;

public interface DishService {

    boolean addDish(AddDishDTO addDishDTO);

    DishesViewInfo getAllDishes();

    void deleteDish(Long id);

    Optional<Dish> findDishById(Long id);
}
