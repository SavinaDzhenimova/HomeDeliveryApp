package com.homedelivery.web;

import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.model.exportDTO.RestaurantDetailsDTO;
import com.homedelivery.service.interfaces.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    void testShowKoronaInfo() throws Exception {
        RestaurantDetailsDTO restaurantDetails = new RestaurantDetailsDTO();
        restaurantDetails.setName(RestaurantName.KORONA);

        when(restaurantService.getRestaurantDetails(RestaurantName.KORONA)).thenReturn(restaurantDetails);

        mockMvc.perform(get("/restaurants/korona"))
                .andExpect(status().isOk())
                .andExpect(view().name("korona"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("restaurant", restaurantDetails));
    }

    @Test
    void testShowVertuInfo() throws Exception {
        RestaurantDetailsDTO restaurantDetails = new RestaurantDetailsDTO();
        restaurantDetails.setName(RestaurantName.VERTU);

        when(restaurantService.getRestaurantDetails(RestaurantName.VERTU)).thenReturn(restaurantDetails);

        mockMvc.perform(get("/restaurants/vertu"))
                .andExpect(status().isOk())
                .andExpect(view().name("vertu"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("restaurant", restaurantDetails));
    }

    @Test
    void testShowKazablankaInfo() throws Exception {
        RestaurantDetailsDTO restaurantDetails = new RestaurantDetailsDTO();
        restaurantDetails.setName(RestaurantName.KAZABLANKA);

        when(restaurantService.getRestaurantDetails(RestaurantName.KAZABLANKA)).thenReturn(restaurantDetails);

        mockMvc.perform(get("/restaurants/kazablanka"))
                .andExpect(status().isOk())
                .andExpect(view().name("kazablanka"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("restaurant", restaurantDetails));
    }

}