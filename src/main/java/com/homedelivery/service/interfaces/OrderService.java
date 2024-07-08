package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.model.user.UserDetailsDTO;

import java.math.BigDecimal;

public interface OrderService {

    void addToCart(Long id, int quantity);

    OrderDishesInfoDTO getAllDishesInCart();

    void removeFromCart(Long id);

    boolean makeOrder(AddOrderDTO addOrderDTO, UserDetailsDTO userDetailsDTO, BigDecimal totalPrice);

    void deleteOrder(Long id);

}
