package com.homedelivery.service;

import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.CategoryName;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.exportDTO.DishDetailsDTO;
import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.model.entity.Category;
import com.homedelivery.model.entity.Dish;
import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.repository.DishRepository;
import com.homedelivery.service.exception.DeleteObjectException;
import com.homedelivery.service.interfaces.CategoryService;
import com.homedelivery.service.interfaces.DishService;
import com.homedelivery.service.interfaces.RestaurantService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public DishServiceImpl(DishRepository dishRepository, RestaurantService restaurantService,
                           CategoryService categoryService, UserService userService, ModelMapper modelMapper) {
        this.dishRepository = dishRepository;
        this.restaurantService = restaurantService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addDish(AddDishDTO addDishDTO) {

        if (addDishDTO == null) {
            return false;
        }

        Optional<Restaurant> optionalRestaurant = this.restaurantService.findByName(addDishDTO.getRestaurant());
        Optional<Category> optionalCategory = this.categoryService.findByName(addDishDTO.getCategory());

        if (optionalCategory.isEmpty() || optionalRestaurant.isEmpty()) {
            return false;
        }

        Category category = optionalCategory.get();
        Restaurant restaurant = optionalRestaurant.get();

        Dish dish = this.modelMapper.map(addDishDTO, Dish.class);
        dish.setRestaurant(restaurant);
        dish.setCategory(category);

        this.dishRepository.saveAndFlush(dish);
        return true;
    }

    @Override
    public DishesViewInfo getAllDishes() {

        List<DishDetailsDTO> salads = this.mapDishToDTO(CategoryName.SALAD);

        List<DishDetailsDTO> starters = this.mapDishToDTO(CategoryName.STARTER);

        List<DishDetailsDTO> mainDishes = this.mapDishToDTO(CategoryName.MAIN_DISH);

        List<DishDetailsDTO> desserts = this.mapDishToDTO(CategoryName.DESSERT);

        return new DishesViewInfo(salads, starters, mainDishes, desserts);
    }

    private List<DishDetailsDTO> mapDishToDTO(CategoryName categoryName) {

        return this.dishRepository.findAll().stream()
                .filter(dish -> dish.getCategory().getName().equals(categoryName))
                .map(dish -> {
                    DishDetailsDTO dto = this.modelMapper.map(dish, DishDetailsDTO.class);
                    dto.setRestaurant(dish.getRestaurant().getName());

                    return dto;
                }).toList();
    }

    @Override
    public void deleteDish(Long id) {

        String username = this.userService.getLoggedUsername();

        Optional<Dish> optionalDish = this.dishRepository.findById(id);
        Optional<User> optionalUser = this.userService.findUserByUsername(username);

        if (optionalDish.isPresent() && optionalUser.isPresent()) {

            User user = optionalUser.get();
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(RoleName.ADMIN));

            if (isAdmin) {
                this.dishRepository.deleteById(id);
            }
        } else {
            throw new DeleteObjectException("You cannot delete dish with id " + id + "!");
        }
    }

    @Override
    public Optional<Dish> findDishById(Long id) {
        return this.dishRepository.findById(id);
    }
}