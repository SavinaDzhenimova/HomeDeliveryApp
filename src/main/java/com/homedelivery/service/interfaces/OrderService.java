package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.exportDTO.OrdersViewInfo;
import com.homedelivery.model.importDTO.AddOrderDTO;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    boolean addToCart(Long id, int quantity);

    OrderDishesInfoDTO getAllDishesInCart();

    boolean removeFromCart(Long id);

    boolean makeOrder(AddOrderDTO addOrderDTO, BigDecimal totalPrice);

    void deleteOrder(Long id);

    List<OrdersViewInfo> getAllOrders();

    boolean progressOrder(Long id);
}
