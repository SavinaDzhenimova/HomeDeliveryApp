package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;

public interface DishService {

    boolean addDish(AddDishDTO addDishDTO);

    DishesViewInfo getAllDishes();

    void deleteDish(Long dishId, Long userId);

}
