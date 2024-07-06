package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;

public interface OrderService {

    boolean addToCart(Long id);

    OrderDishesInfoDTO getAllDishesInCart();

    void removeDishFromCart(Long id);

}
