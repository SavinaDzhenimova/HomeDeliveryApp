package com.homedelivery.service;

import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.entity.Order;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.OrderStatus;
import com.homedelivery.service.events.MakeOrderEvent;
import com.homedelivery.model.exportDTO.OrderDishDetailsDTO;
import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.exportDTO.OrdersViewInfo;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.repository.OrderRepository;
import com.homedelivery.service.exception.BadOrderRequestException;
import com.homedelivery.service.exception.DeleteObjectException;
import com.homedelivery.service.interfaces.DishService;
import com.homedelivery.service.interfaces.OrderService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;
    private final DishService dishService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private Map<Long, Integer> dishesToOrderMap;

    public OrderServiceImpl(ApplicationEventPublisher applicationEventPublisher, OrderRepository orderRepository,
                            DishService dishService, UserService userService, ModelMapper modelMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderRepository = orderRepository;
        this.dishService = dishService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.dishesToOrderMap = new HashMap<>();
    }

    @Override
    public boolean addToCart(Long id, int quantity) {

        if (quantity >= 1) {
            this.dishesToOrderMap.put(id, quantity);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeFromCart(Long id) {
        Optional<Dish> optionalDish = this.dishService.findDishById(id);

        if (optionalDish.isPresent()) {
            this.dishesToOrderMap.remove(id);
            return true;
        }

        return false;
    }

    @Override
    public boolean makeOrder(AddOrderDTO addOrderDTO, BigDecimal totalPrice) {
        String deliveryAddress = addOrderDTO.getDeliveryAddress();
        String phoneNumber = addOrderDTO.getPhoneNumber();

        if (deliveryAddress == null || deliveryAddress.length() < 3 || deliveryAddress.length() > 100) {
            throw new BadOrderRequestException("delivery address");
        }

        if (phoneNumber == null || phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            throw new BadOrderRequestException("phone number");
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        String username = this.userService.getLoggedUsername();

        Optional<User> optionalUser = this.userService.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        Order order = this.mapToOrder(user, totalPrice, addOrderDTO);

        this.orderRepository.saveAndFlush(order);
        this.dishesToOrderMap.clear();

        this.applicationEventPublisher.publishEvent(
                new MakeOrderEvent(this, user.getEmail(), user.getFullName(), order.getId(),
                        order.getTotalPrice(), order.getDeliveryAddress(), order.getPhoneNumber()));

        return true;
    }

    @Override
    public void deleteOrder(Long id) {
        String username = this.userService.getLoggedUsername();

        Optional<Order> optionalOrder = this.orderRepository.findById(id);
        Optional<User> optionalUser = this.userService.findUserByUsername(username);

        if (optionalOrder.isPresent() && optionalUser.isPresent()) {

            Order order = optionalOrder.get();

            if (order.getStatus().equals(OrderStatus.DELIVERED) && order.getClient().getUsername().equals(username)) {
                this.orderRepository.deleteById(id);
            } else {
                throw new DeleteObjectException("You cannot delete order until it is not delivered yet!");
            }

        } else {
            throw new DeleteObjectException("You cannot delete order with id " + id + "!");
        }
    }

    @Override
    public List<OrdersViewInfo> getAllOrders() {

        return this.orderRepository.findAll().stream()
                .map(order -> {
                    OrdersViewInfo dto = this.modelMapper.map(order, OrdersViewInfo.class);
                    dto.setOrderedBy(order.getClient().getUsername());
                    dto.setOrderedOn(order.parseDateToString(order.getOrderedOn()));

                    String deliveredOn = (order.getDeliveredOn() == null)
                            ? "-"
                            : order.parseDateToString(order.getDeliveredOn());

                    dto.setDeliveredOn(deliveredOn);
                    dto.setStatus(order.getStatus().name());

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public boolean progressOrder(Long id) {

        Optional<Order> optionalOrder = this.orderRepository.findById(id);

        if (optionalOrder.isPresent()) {

            Order order = optionalOrder.get();

            if (order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
                order.setStatus(OrderStatus.DELIVERED);
                order.setDeliveredOn(LocalDateTime.now());
                this.orderRepository.save(order);

                return true;
            }
        }

        return false;
    }

    @Override
    public OrderDishesInfoDTO getAllDishesInCart() {
        List<OrderDishDetailsDTO> dishesToOrderList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : this.dishesToOrderMap.entrySet()) {

            Long id = entry.getKey();
            int quantity = entry.getValue();

            Optional<Dish> optionalDish = this.dishService.findDishById(id);

            if (optionalDish.isPresent()) {

                Dish dish = optionalDish.get();

                OrderDishDetailsDTO dishDetailsDTO = this.modelMapper.map(dish, OrderDishDetailsDTO.class);
                dishDetailsDTO.setQuantity(quantity);
                dishDetailsDTO.setTotalPrice(dish.getPrice().multiply(BigDecimal.valueOf(quantity)));

                dishesToOrderList.add(dishDetailsDTO);
            }
        }

        return new OrderDishesInfoDTO(dishesToOrderList);
    }

    private Order mapToOrder(User user, BigDecimal totalPrice, AddOrderDTO addOrderDTO) {
        Order order = this.modelMapper.map(addOrderDTO, Order.class);

        List<OrderDishDetailsDTO> dishesToOrder = new ArrayList<>();

        this.getAllDishesInCart().getDishesToOrder()
                .forEach(orderDishDetailsDTO -> {
                    int quantity = orderDishDetailsDTO.getQuantity();

                    for (int i = 1; i <= quantity; i++) {
                        dishesToOrder.add(orderDishDetailsDTO);
                    }
                });

        List<Dish> dishes = dishesToOrder.stream()
                .map(orderDishDetailsDTO ->
                        this.modelMapper.map(orderDishDetailsDTO, Dish.class)).toList();

        order.setTotalPrice(totalPrice);
        order.setOrderedOn(LocalDateTime.now());
        order.setClient(user);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setDishes(dishes);

        return order;
    }
}