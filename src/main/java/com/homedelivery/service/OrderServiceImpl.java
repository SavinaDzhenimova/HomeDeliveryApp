package com.homedelivery.service;

import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.exportDTO.OrderDishDetailsDTO;
import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.repository.OrderRepository;
import com.homedelivery.service.interfaces.DishService;
import com.homedelivery.service.interfaces.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishService dishService;
    private final ModelMapper modelMapper;
    private List<OrderDishDetailsDTO> dishesToOrder;

    public OrderServiceImpl(OrderRepository orderRepository, DishService dishService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.dishService = dishService;
        this.modelMapper = modelMapper;
        this.dishesToOrder = new ArrayList<>();
    }

    @Override
    public boolean addToCart(Long id) {

        Optional<Dish> optionalDish = this.dishService.findDishById(id);

        if (optionalDish.isEmpty()) {
            return false;
        }

        Dish dish = optionalDish.get();

        OrderDishDetailsDTO orderDishDetailsDTO = this.modelMapper.map(dish, OrderDishDetailsDTO.class);

        this.dishesToOrder.add(orderDishDetailsDTO);

        return true;
    }

    @Override
    public OrderDishesInfoDTO getAllDishesInCart() {
        return new OrderDishesInfoDTO(this.dishesToOrder);
    }

    @Override
    public void removeDishFromCart(Long id) {
        Optional<Dish> optionalDish = this.dishService.findDishById(id);

        if (optionalDish.isPresent()) {
            Dish dish = optionalDish.get();

            this.dishesToOrder.removeIf(dto -> dto.getId().equals(id));
        }
    }
}