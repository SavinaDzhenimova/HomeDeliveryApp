package com.homedelivery.service.interfaces;

import com.homedelivery.model.dto.RestaurantDetailsDTO;
import com.homedelivery.model.enums.RestaurantName;

public interface RestaurantService {

    RestaurantDetailsDTO getRestaurantDetails(RestaurantName name);
}