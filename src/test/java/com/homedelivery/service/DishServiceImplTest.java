package com.homedelivery.service;

import com.homedelivery.model.entity.*;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.exportDTO.DishDetailsDTO;
import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.repository.DishRepository;
import com.homedelivery.service.exception.DeleteObjectException;
import com.homedelivery.service.interfaces.CategoryService;
import com.homedelivery.service.interfaces.RestaurantService;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @Mock
    private DishRepository mockDishRepository;

    @Mock
    private RestaurantService mockRestaurantService;

    @Mock
    private CategoryService mockCategoryService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper modelMapper;

    private DishServiceImpl dishService;

    @BeforeEach
    public void setUp() {
        dishService = new DishServiceImpl(mockDishRepository, mockRestaurantService,
                mockCategoryService, mockUserService, modelMapper);
    }

    @Test
    public void testAddDish() {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(RestaurantName.KORONA);
        Category category = new Category();
        category.setName(CategoryName.DESSERT);

        AddDishDTO addDishDTO = new AddDishDTO();
        addDishDTO.setRestaurant(restaurant.getName());
        addDishDTO.setCategory(category.getName());

        Dish dish = new Dish();

        when(mockRestaurantService.findByName(RestaurantName.KORONA)).thenReturn(Optional.of(restaurant));
        when(mockCategoryService.findByName(CategoryName.DESSERT)).thenReturn(Optional.of(category));
        when(modelMapper.map(addDishDTO, Dish.class)).thenReturn(dish);
        when(mockDishRepository.saveAndFlush(dish)).thenReturn(dish);

        boolean result = dishService.addDish(addDishDTO);

        assertTrue(result);
        assertEquals(dish.getRestaurant(), restaurant);
        assertEquals(dish.getCategory(), category);
        verify(mockDishRepository, times(1)).saveAndFlush(dish);
    }

    @Test
    public void testAddDish_Failure_NullDTO() {
        boolean result = dishService.addDish(null);

        assertFalse(result);
        verify(mockDishRepository, times(0)).saveAndFlush(any(Dish.class));
    }

    @Test
    public void testAddDish_Failure_CategoryOrRestaurantNotFound() {
        AddDishDTO addDishDTO = new AddDishDTO();

        when(mockRestaurantService.findByName(addDishDTO.getRestaurant())).thenReturn(Optional.empty());
        when(mockCategoryService.findByName(addDishDTO.getCategory())).thenReturn(Optional.empty());

        boolean result = dishService.addDish(addDishDTO);

        assertFalse(result);
        verify(mockDishRepository, times(0)).saveAndFlush(any(Dish.class));
    }

    @Test
    public void testDeleteDish_Success() {
        Dish dish = new Dish();
        User adminUser = new User();

        Role admin = new Role();
        admin.setName(RoleName.ADMIN);

        adminUser.setRoles(Collections.singletonList(admin));
        adminUser.setUsername("admin");

        when(mockDishRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        when(mockUserService.getLoggedUsername()).thenReturn("admin");
        when(mockUserService.findUserByUsername("admin")).thenReturn(Optional.of(adminUser));

        dishService.deleteDish(1L);

        verify(mockDishRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteDish_Failure_NotAdmin() {
        Dish dish = new Dish();
        User testUser = new User();

        Role user = new Role();
        user.setName(RoleName.USER);

        testUser.setRoles(Collections.singletonList(user));
        testUser.setUsername("testUser");

        when(mockDishRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        when(mockUserService.getLoggedUsername()).thenReturn("testUser");
        when(mockUserService.findUserByUsername("testUser")).thenReturn(Optional.of(testUser));

        dishService.deleteDish(1L);

        verify(mockDishRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testDeleteDish_Failure_DishNotFound() {
        User adminUser = new User();

        Role admin = new Role();
        admin.setName(RoleName.ADMIN);

        adminUser.setRoles(Collections.singletonList(admin));
        adminUser.setUsername("admin");

        when(mockDishRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(mockUserService.getLoggedUsername()).thenReturn("admin");
        when(mockUserService.findUserByUsername("admin")).thenReturn(Optional.of(adminUser));

        dishService.deleteDish(1L);

        verify(mockDishRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testFindDishById_Success() {
        Dish dish = new Dish();

        when(mockDishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Optional<Dish> result = dishService.findDishById(1L);

        assertTrue(result.isPresent());
        assertEquals(dish, result.get());
    }

    @Test
    public void testFindDishById_Failure() {
        when(mockDishRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Dish> result = dishService.findDishById(1L);

        assertFalse(result.isPresent());
    }
}