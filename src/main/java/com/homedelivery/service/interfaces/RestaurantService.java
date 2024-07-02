package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.RestaurantDetailsDTO;
import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.model.enums.RestaurantName;

import java.util.Optional;

public interface RestaurantService {

    RestaurantDetailsDTO getRestaurantDetails(RestaurantName name);

    Optional<Restaurant> findByName(RestaurantName name);
}