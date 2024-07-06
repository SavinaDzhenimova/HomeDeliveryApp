package com.homedelivery.model.exportDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderDishesInfoDTO {

    private List<OrderDishDetailsDTO> dishesToOrder;

    private int totalDishesCount;

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
        return this.dishesToOrder.size();
    }

    public void setTotalDishesCount(int totalDishesCount) {
        this.totalDishesCount = totalDishesCount;
    }
}
