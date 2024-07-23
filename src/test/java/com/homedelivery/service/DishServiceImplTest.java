package com.homedelivery.service;

import com.homedelivery.model.entity.*;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.exportDTO.DishDetailsDTO;
import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.repository.DishRepository;
import com.homedelivery.service.interfaces.CategoryService;
import com.homedelivery.service.interfaces.RestaurantService;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private DishServiceImpl dishServiceToTest;

    @BeforeEach
    public void setUp() {
        dishServiceToTest = new DishServiceImpl(mockDishRepository, mockRestaurantService,
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

        boolean result = dishServiceToTest.addDish(addDishDTO);

        assertTrue(result);
        assertEquals(dish.getRestaurant(), restaurant);
        assertEquals(dish.getCategory(), category);
        verify(mockDishRepository, times(1)).saveAndFlush(dish);
    }

    @Test
    public void testAddDish_Failure_NullDTO() {
        boolean result = dishServiceToTest.addDish(null);

        assertFalse(result);
        verify(mockDishRepository, times(0)).saveAndFlush(any(Dish.class));
    }

    @Test
    public void testAddDish_Failure_CategoryOrRestaurantNotFound() {
        AddDishDTO addDishDTO = new AddDishDTO();

        when(mockRestaurantService.findByName(addDishDTO.getRestaurant())).thenReturn(Optional.empty());
        when(mockCategoryService.findByName(addDishDTO.getCategory())).thenReturn(Optional.empty());

        boolean result = dishServiceToTest.addDish(addDishDTO);

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

        dishServiceToTest.deleteDish(1L);

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

        dishServiceToTest.deleteDish(1L);

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

        dishServiceToTest.deleteDish(1L);

        verify(mockDishRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testFindDishById_Success() {
        Dish dish = new Dish();

        when(mockDishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Optional<Dish> result = dishServiceToTest.findDishById(1L);

        assertTrue(result.isPresent());
        assertEquals(dish, result.get());
    }

    @Test
    public void testFindDishById_Failure() {
        when(mockDishRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Dish> result = dishServiceToTest.findDishById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testMapDishToDTO() {
        Category category = new Category();
        category.setName(CategoryName.SALAD);
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RestaurantName.KAZABLANKA);

        Dish dish1 = new Dish();
        dish1.setCategory(category);
        dish1.setRestaurant(restaurant);
        Dish dish2 = new Dish();
        dish2.setCategory(category);
        dish2.setRestaurant(restaurant);

        when(mockDishRepository.findAll()).thenReturn(Stream.of(dish1, dish2).collect(Collectors.toList()));
        when(modelMapper.map(any(Dish.class), eq(DishDetailsDTO.class))).thenAnswer(invocation -> {
            Dish dish = invocation.getArgument(0);
            DishDetailsDTO dto = new DishDetailsDTO();
            dto.setRestaurant(dish.getRestaurant().getName());

            return dto;
        });

        List<DishDetailsDTO> result = dishServiceToTest.mapDishToDTO(CategoryName.SALAD);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(restaurant.getName(), result.get(0).getRestaurant());
        assertEquals(restaurant.getName(), result.get(1).getRestaurant());
    }

    @Test
    public void testGetAllDishes() {
        Dish saladDish = createDish(CategoryName.SALAD, RestaurantName.KAZABLANKA);
        Dish starterDish = createDish(CategoryName.STARTER, RestaurantName.KORONA);
        Dish mainDish = createDish(CategoryName.MAIN_DISH, RestaurantName.VERTU);
        Dish dessertDish = createDish(CategoryName.DESSERT, RestaurantName.KAZABLANKA);

        List<Dish> dishes = Arrays.asList(saladDish, starterDish, mainDish, dessertDish);

        when(mockDishRepository.findAll()).thenReturn(dishes);
        when(modelMapper.map(any(Dish.class), eq(DishDetailsDTO.class))).thenAnswer(invocation -> {
            Dish dish = invocation.getArgument(0);
            DishDetailsDTO dto = new DishDetailsDTO();
            dto.setName(dish.getName());
            dto.setRestaurant(dish.getRestaurant().getName());

            return dto;
        });

        DishesViewInfo dishesViewInfo = dishServiceToTest.getAllDishes();

        assertNotNull(dishesViewInfo);
        assertEquals(1, dishesViewInfo.getSalads().size());
        assertEquals(1, dishesViewInfo.getStarters().size());
        assertEquals(1, dishesViewInfo.getMainDishes().size());
        assertEquals(1, dishesViewInfo.getDesserts().size());

        assertEquals(RestaurantName.KAZABLANKA, dishesViewInfo.getSalads().get(0).getRestaurant());
        assertEquals(RestaurantName.KORONA, dishesViewInfo.getStarters().get(0).getRestaurant());
        assertEquals(RestaurantName.VERTU, dishesViewInfo.getMainDishes().get(0).getRestaurant());
        assertEquals(RestaurantName.KAZABLANKA, dishesViewInfo.getDesserts().get(0).getRestaurant());

        verify(mockDishRepository, times(4)).findAll();
        verify(modelMapper, times(4)).map(any(Dish.class), eq(DishDetailsDTO.class));
    }

    private Dish createDish(CategoryName categoryName, RestaurantName restaurantName) {
        Dish dish = new Dish();
        dish.setName(categoryName.name() + " Dish");

        Category category = new Category();
        category.setName(categoryName);
        dish.setCategory(category);

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        dish.setRestaurant(restaurant);

        return dish;
    }
}