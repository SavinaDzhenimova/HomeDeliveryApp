package com.homedelivery.service;

import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.entity.Order;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.OrderStatus;
import com.homedelivery.model.exportDTO.OrderDishDetailsDTO;
import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.exportDTO.OrdersViewInfo;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.repository.OrderRepository;
import com.homedelivery.service.events.MakeOrderEvent;
import com.homedelivery.service.exception.BadOrderRequestException;
import com.homedelivery.service.exception.DeleteObjectException;
import com.homedelivery.service.interfaces.DishService;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderServiceImpl orderServiceToTest;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private DishService mockDishService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    public void setUp() {
        orderServiceToTest = new OrderServiceImpl(applicationEventPublisher, mockOrderRepository,
                mockDishService, mockUserService, mockModelMapper);
    }

    @Test
    public void testAddToCart_Successful() {
        Long dishId = 1L;
        int quantity = 3;

        Dish dishToAdd = new Dish();
        dishToAdd.setId(dishId);

        boolean result = orderServiceToTest.addToCart(dishId, quantity);

        assertTrue(result);
    }

    @Test
    public void testAddToCart_InvalidQuantity() {
        Long dishId = 1L;
        int quantity = 0;

        Dish dishToAdd = new Dish();
        dishToAdd.setId(dishId);

        boolean result = orderServiceToTest.addToCart(dishId, quantity);

        assertFalse(result);
    }

    @Test
    public void testRemoveFromCart_Successful() {
        Long dishId = 1L;
        int quantity = 3;

        orderServiceToTest.addToCart(dishId, quantity);

        when(mockDishService.findDishById(dishId)).thenReturn(Optional.of(new Dish()));

        boolean result = orderServiceToTest.removeFromCart(dishId);

        assertTrue(result);
        assertFalse(orderServiceToTest.getAllDishesInCart()
                .getDishesToOrder().stream()
                .anyMatch(dish -> dish.getId().equals(dishId)));
    }

    @Test
    public void testRemoveFromCart_DishNotPresent() {
        Long dishId = 999L;
        int quantity = 3;

        orderServiceToTest.addToCart(dishId, quantity);

        when(mockDishService.findDishById(dishId)).thenReturn(Optional.empty());

        boolean result = orderServiceToTest.removeFromCart(dishId);

        assertFalse(result);
    }

    @Test
    public void testMakeOrder_Successful() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setDeliveryAddress("Test Address");
        addOrderDTO.setPhoneNumber("1234567");
        BigDecimal totalPrice = new BigDecimal("50.00");

        User user = new User();
        user.setUsername("testuser");

        when(mockUserService.getLoggedUsername()).thenReturn("testuser");
        when(mockUserService.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(mockOrderRepository.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockModelMapper.map(any(AddOrderDTO.class), eq(Order.class))).thenAnswer(invocation -> {
            AddOrderDTO source = invocation.getArgument(0);
            Order order = new Order();
            order.setDeliveryAddress(source.getDeliveryAddress());
            order.setPhoneNumber(source.getPhoneNumber());

            return order;
        });

        boolean result = orderServiceToTest.makeOrder(addOrderDTO, totalPrice);

        assertTrue(result);
        verify(mockOrderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(MakeOrderEvent.class));
    }

    @Test
    public void testMakeOrder_NullPrice() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setDeliveryAddress("Test Address");
        addOrderDTO.setPhoneNumber("1234567");
        BigDecimal totalPrice = new BigDecimal("0");

        boolean result = orderServiceToTest.makeOrder(addOrderDTO, totalPrice);

        assertFalse(result);
        verify(mockOrderRepository, times(0)).saveAndFlush(any(Order.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(MakeOrderEvent.class));
    }

    @Test
    public void testMakeOrder_NullDeliveryAddress() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setDeliveryAddress("");
        addOrderDTO.setPhoneNumber("111222333");
        BigDecimal totalPrice = new BigDecimal("100");

        BadOrderRequestException exception = assertThrows(BadOrderRequestException.class, () ->
                orderServiceToTest.makeOrder(addOrderDTO, totalPrice));

        assertEquals("delivery address", exception.getParameter());
        verify(mockOrderRepository, times(0)).saveAndFlush(any(Order.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(MakeOrderEvent.class));
    }

    @Test
    public void testMakeOrder_NullPhoneNumber() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setDeliveryAddress("Test Address");
        addOrderDTO.setPhoneNumber("");
        BigDecimal totalPrice = new BigDecimal("100");

        BadOrderRequestException exception = assertThrows(BadOrderRequestException.class, () ->
                orderServiceToTest.makeOrder(addOrderDTO, totalPrice));

        assertEquals("phone number", exception.getParameter());
        verify(mockOrderRepository, times(0)).saveAndFlush(any(Order.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(MakeOrderEvent.class));
    }


    @Test
    public void testDeleteOrder_Successful() {
        Long orderId = 1L;
        User user = new User();
        user.setUsername("testuser");

        Order order = new Order();
        order.setId(orderId);
        order.setClient(user);
        order.setStatus(OrderStatus.DELIVERED);

        when(mockUserService.getLoggedUsername()).thenReturn("testuser");
        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mockUserService.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> orderServiceToTest.deleteOrder(orderId));
        verify(mockOrderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testDeleteOrder_UserNotFound() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setDeliveryAddress("Test address");
        addOrderDTO.setPhoneNumber("111222333");
        BigDecimal totalPrice = new BigDecimal("100");

        boolean result = orderServiceToTest.makeOrder(addOrderDTO, totalPrice);

        assertFalse(result);
        verify(mockOrderRepository, times(0)).saveAndFlush(any(Order.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(MakeOrderEvent.class));
    }

    @Test
    public void testDeleteOrder_OrderInProgress_ThrowsException() {
        Long orderId = 1L;
        User user = new User();
        user.setUsername("testuser");

        Order order = new Order();
        order.setId(orderId);
        order.setClient(user);
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(mockUserService.getLoggedUsername()).thenReturn("testuser");
        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mockUserService.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(DeleteObjectException.class, () ->
                orderServiceToTest.deleteOrder(orderId));

        String expectedMessage = "You cannot delete order until it is not delivered yet!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(mockOrderRepository, times(0)).deleteById(orderId);
    }

    @Test
    public void testDeleteOrder_OrderNotFound_ThrowsException() {
        Long orderId = 999L;

        when(mockUserService.getLoggedUsername()).thenReturn("testuser");
        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DeleteObjectException.class, () ->
                orderServiceToTest.deleteOrder(orderId));

        String expectedMessage = "You cannot delete order with id 999!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(mockOrderRepository, times(0)).deleteById(orderId);
    }

    @Test
    public void testGetAllOrders_Successful() {
        User client = new User();
        client.setUsername("client");

        Order order1 = new Order();
        order1.setClient(client);
        order1.setOrderedOn(LocalDateTime.of(2024, 6, 20, 14, 8));
        order1.setDeliveredOn(null);
        order1.setStatus(OrderStatus.IN_PROGRESS);

        Order order2 = new Order();
        order2.setClient(client);
        order2.setOrderedOn(LocalDateTime.of(2024, 5, 19, 11, 54));
        order2.setDeliveredOn(LocalDateTime.of(2024, 5, 19, 12, 4));
        order2.setStatus(OrderStatus.DELIVERED);

        List<Order> orders = Arrays.asList(order1, order2);

        OrdersViewInfo dto1 = new OrdersViewInfo();

        OrdersViewInfo dto2 = new OrdersViewInfo();

        when(mockOrderRepository.findAll()).thenReturn(orders);

        when(mockModelMapper.map(order1, OrdersViewInfo.class)).thenReturn(dto1);
        when(mockModelMapper.map(order2, OrdersViewInfo.class)).thenReturn(dto2);

        List<OrdersViewInfo> result = orderServiceToTest.getAllOrders();

        assertEquals(2, result.size());
        assertEquals("client", result.get(0).getOrderedBy());
        assertEquals("2024-06-20 14:08:00", result.get(0).getOrderedOn());
        assertEquals("-", result.get(0).getDeliveredOn());
        assertEquals("IN_PROGRESS", result.get(0).getStatus());

        assertEquals("client", result.get(1).getOrderedBy());
        assertEquals("2024-05-19 11:54:00", result.get(1).getOrderedOn());
        assertEquals("2024-05-19 12:04:00", result.get(1).getDeliveredOn());
        assertEquals("DELIVERED", result.get(1).getStatus());
    }

    @Test
    public void testProgressOrder_Successful() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        boolean result = orderServiceToTest.progressOrder(orderId);

        assertTrue(result);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(mockOrderRepository, times(1)).save(order);
    }

    @Test
    public void testProgressOrder_AlreadyDeliveredOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.DELIVERED);

        when(mockOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        boolean result = orderServiceToTest.progressOrder(orderId);

        assertFalse(result);
        verify(mockOrderRepository, times(0)).save(order);
    }

    @Test
    public void testGetAllDishesInCart_Successful() {
        Long dishId = 1L;
        int quantity = 2;
        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setPrice(new BigDecimal("10.00"));

        orderServiceToTest.addToCart(dishId, quantity);
        when(mockDishService.findDishById(dishId)).thenReturn(Optional.of(dish));
        when(mockModelMapper.map(any(Dish.class), eq(OrderDishDetailsDTO.class))).thenAnswer(invocation -> {
            Dish source = invocation.getArgument(0);
            OrderDishDetailsDTO dto = new OrderDishDetailsDTO();
            dto.setId(source.getId());
            dto.setTotalPrice(source.getPrice());
            return dto;
        });

        OrderDishesInfoDTO result = orderServiceToTest.getAllDishesInCart();

        assertEquals(1, result.getDishesToOrder().size());
        assertEquals(dishId, result.getDishesToOrder().get(0).getId());
        assertEquals(quantity, result.getDishesToOrder().get(0).getQuantity());
        assertEquals(new BigDecimal("20.00"), result.getDishesToOrder().get(0).getTotalPrice());
    }

}