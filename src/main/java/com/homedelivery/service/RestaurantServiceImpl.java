package com.homedelivery.service;

import com.homedelivery.model.exportDTO.RestaurantDetailsDTO;
import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.repository.RestaurantRepository;
import com.homedelivery.service.exception.ObjectNotFoundException;
import com.homedelivery.service.interfaces.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RestaurantDetailsDTO getRestaurantDetails(RestaurantName name) {

        Optional<Restaurant> optionalRestaurant = this.findByName(name);

        if (optionalRestaurant.isEmpty()) {
            return null;
        }

        return this.modelMapper.map(optionalRestaurant.get(), RestaurantDetailsDTO.class);
    }

    @Override
    public Optional<Restaurant> findByName(RestaurantName name) {
        return this.restaurantRepository.findByName(name);
    }
}