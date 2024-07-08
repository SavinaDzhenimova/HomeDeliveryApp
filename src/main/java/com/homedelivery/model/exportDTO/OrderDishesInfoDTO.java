package com.homedelivery.model.exportDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDishesInfoDTO {

    private List<OrderDishDetailsDTO> dishesToOrder;

    private int totalDishesCount;

    private BigDecimal totalPrice;

    public OrderDishesInfoDTO() {
        this.dishesToOrder = new ArrayList<>();
    }

    public OrderDishesInfoDTO(List<OrderDishDetailsDTO> dishesToOrder) {
        this.dishesToOrder = dishesToOrder;
    }

    public List<OrderDishDetailsDTO> getDishesToOrder() {
        return dishesToOrder;
    }

    public void setDishesToOrder(List<OrderDishDetailsDTO> dishesToOrder) {
        this.dishesToOrder = dishesToOrder;
    }

    public int getTotalDishesCount() {
        return this.dishesToOrder.stream()
                .map(OrderDishDetailsDTO::getQuantity)
                .reduce(0, Integer::sum);
    }

    public void setTotalDishesCount(int totalDishesCount) {
        this.totalDishesCount = totalDishesCount;
    }

    public BigDecimal getTotalPrice() {
        return this.dishesToOrder.stream()
                .map(OrderDishDetailsDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
