package com.homedelivery.web;

import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.service.interfaces.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
class DishControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddDishGet() throws Exception {
        mockMvc.perform(get("/dishes/add-dish"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-dish"))
                .andExpect(model().attributeExists("addDishDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddDishPostSuccess() throws Exception {
        when(dishService.addDish(any(AddDishDTO.class))).thenReturn(true);

        mockMvc.perform(post("/dishes/add-dish")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Pizza")
                        .param("description", "Delicious cheese pizza")
                        .param("price", "9.99")
                        .param("imageUrl", "https://dish.jpg")
                        .param("category", "MAIN_DISH")
                        .param("restaurant", "KORONA")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dishes/menu"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(dishService).addDish(any(AddDishDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddDishPostValidationError() throws Exception {
        mockMvc.perform(post("/dishes/add-dish")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")
                        .param("description", "")
                        .param("price", "-1.0")
                        .param("imageUrl", "https://dish.jpg")
                        .param("category", "MAIN_DISH")
                        .param("restaurant", "KORONA")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-dish"))
                .andExpect(model().attributeExists("addDishDTO"))
                .andExpect(model().attributeHasFieldErrors("addDishDTO", "name", "description", "price"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddDishPostServiceError() throws Exception {
        when(dishService.addDish(any(AddDishDTO.class))).thenReturn(false);

        mockMvc.perform(post("/dishes/add-dish")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Pizza")
                        .param("description", "Delicious cheese pizza")
                        .param("price", "9.99")
                        .param("imageUrl", "https://dish.jpg")
                        .param("category", "MAIN_DISH")
                        .param("restaurant", "KORONA")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-dish"));

        verify(dishService).addDish(any(AddDishDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testViewMenu() throws Exception {
        DishesViewInfo dishesViewInfo = new DishesViewInfo();
        dishesViewInfo.setMainDishes(Collections.emptyList());
        when(dishService.getAllDishes()).thenReturn(dishesViewInfo);

        mockMvc.perform(get("/dishes/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attributeExists("dishes"))
                .andExpect(model().attribute("dishes", dishesViewInfo));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testDeleteDish() throws Exception {
        mockMvc.perform(delete("/dishes/menu/delete-dish/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dishes/menu"));

        verify(dishService).deleteDish(1L);
    }

}