package com.homedelivery.web;

import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.exportDTO.OrdersViewInfo;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.service.interfaces.OrderService;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testViewMakeOrder() throws Exception {
        OrderDishesInfoDTO orderDishesInfoDTO = new OrderDishesInfoDTO();
        when(orderService.getAllDishesInCart()).thenReturn(orderDishesInfoDTO);
        when(userService.getLoggedUsername()).thenReturn("testuser");

        mockMvc.perform(get("/orders/make-order"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("addOrderDTO"))
                .andExpect(model().attribute("username", "testuser"))
                .andExpect(model().attribute("cartDishes", orderDishesInfoDTO))
                .andExpect(view().name("make-order"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testMakeOrder_withValidData() throws Exception {
        when(orderService.makeOrder(any(AddOrderDTO.class), any(BigDecimal.class))).thenReturn(true);

        mockMvc.perform(post("/orders/make-order/{totalPrice}", "100")
                        .param("deliveryAddress", "Correct Address")
                        .param("phoneNumber", "111222333")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testMakeOrder_withInvalidTotalPrice() throws Exception {
        mockMvc.perform(post("/orders/make-order/{totalPrice}", "0")
                        .param("deliveryAddress", "Correct Address")
                        .param("phoneNumber", "111222333")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(view().name("redirect:/orders/make-order"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/delete-order/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddToCart() throws Exception {
        mockMvc.perform(post("/orders/add-to-cart/{id}", 1L)
                        .param("quantity", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dishes/menu"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveFromCart() throws Exception {
        mockMvc.perform(get("/orders/remove-from-cart/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/make-order"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testGetAllOrders() throws Exception {
        List<OrdersViewInfo> ordersViewInfoList = new ArrayList<>();
        when(orderService.getAllOrders()).thenReturn(ordersViewInfoList);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("ordersCount", ordersViewInfoList.size()))
                .andExpect(view().name("orders"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testProgressOrder_withValidData() throws Exception {
        when(orderService.progressOrder(1L)).thenReturn(true);

        mockMvc.perform(post("/orders/progress-order/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testProgressOrder_withInvalidData() throws Exception {
        when(orderService.progressOrder(1L)).thenReturn(false);

        mockMvc.perform(post("/orders/progress-order/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(view().name("redirect:/orders"));
    }

}