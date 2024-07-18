package com.homedelivery.service;

import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.model.exportDTO.RestaurantDetailsDTO;
import com.homedelivery.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    private RestaurantServiceImpl restaurantServiceToTest;

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        this.restaurantServiceToTest = new RestaurantServiceImpl(mockRestaurantRepository, modelMapper);
    }

    @Test
    public void testGetRestaurantDetails_Found() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RestaurantName.KORONA);

        RestaurantDetailsDTO restaurantDetailsDTO = new RestaurantDetailsDTO();
        restaurantDetailsDTO.setName(RestaurantName.KORONA);

        when(mockRestaurantRepository.findByName(RestaurantName.KORONA)).thenReturn(Optional.of(restaurant));
        when(modelMapper.map(restaurant, RestaurantDetailsDTO.class)).thenReturn(restaurantDetailsDTO);

        RestaurantDetailsDTO foundRestaurantDetails = restaurantServiceToTest.getRestaurantDetails(RestaurantName.KORONA);

        assertEquals(restaurantDetailsDTO, foundRestaurantDetails);
    }

    @Test
    public void testGetRestaurantDetails_NotFound() {
        Restaurant restaurant = new Restaurant();

        when(mockRestaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());

        RestaurantDetailsDTO foundRestaurantDetails = restaurantServiceToTest.getRestaurantDetails(restaurant.getName());

        assertNull(foundRestaurantDetails);
    }

    @Test
    public void testFindByName_Found() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RestaurantName.KAZABLANKA);

        when(mockRestaurantRepository.findByName(RestaurantName.KAZABLANKA)).thenReturn(Optional.of(restaurant));

        Optional<Restaurant> foundRestaurant = restaurantServiceToTest.findByName(RestaurantName.KAZABLANKA);

        assertEquals(RestaurantName.KAZABLANKA, foundRestaurant.get().getName());
    }

    @Test
    public void testFindByName_NotFound() {
        Restaurant restaurant = new Restaurant();

        when(mockRestaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());

        Optional<Restaurant> foundRestaurant = restaurantServiceToTest.findByName(restaurant.getName());

        assertFalse(foundRestaurant.isPresent());
    }

}