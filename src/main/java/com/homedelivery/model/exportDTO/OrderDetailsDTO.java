package com.homedelivery.model.exportDTO;

import com.homedelivery.model.enums.OrderStatus;

public class OrderDetailsDTO {

    private Long id;

    private String orderedOn;

    private OrderStatus status;

    public OrderDetailsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
