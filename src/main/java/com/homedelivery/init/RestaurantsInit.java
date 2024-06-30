package com.homedelivery.init;

import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;

@Component
public class RestaurantsInit implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    public RestaurantsInit(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void run(String... args) {

        if (this.restaurantRepository.count() == 0) {

            Arrays.stream(RestaurantName.values())
                    .forEach(restaurantName -> {
                        Restaurant restaurant = new Restaurant();
                        restaurant.setName(restaurantName);

                        switch (restaurantName) {
                            case KORONA -> {
                                restaurant.setDescription("The complex is opened all year round and is suitable for family holidays, as well as business trips. It has a restaurant, swimming pool, completely renovated summer garden and parking.");
                                restaurant.setAddress("Str. Republika 5, Madan 4900, Bulgaria");
                                restaurant.setPhoneNumber("089 362 7306");
                                restaurant.setEmail("korona_madan@abv.bg");
                                restaurant.setOpen(LocalTime.of(7, 0));
                                restaurant.setClose(LocalTime.of(23, 0));
                                restaurant.setImageUrl("/images/korona/korona.jpg");
                            }
                            case VERTU -> {
                                restaurant.setDescription("Here you will be offered aromatic coffee, refreshing drinks and meats. The stylish interior is completely subordinated to the idea of convenience, which offers opportunity for a pleasant vacation.");
                                restaurant.setAddress("Str. Obedinenie 5, Madan 4900, Bulgaria");
                                restaurant.setPhoneNumber("089 900 7373");
                                restaurant.setEmail("vertu_madan@abv.bg");
                                restaurant.setOpen(LocalTime.of(8, 0));
                                restaurant.setClose(LocalTime.of(0, 0));
                                restaurant.setImageUrl("/images/vertu/vertu.jpg");

                            }
                            case KAZABLANKA -> {
                                restaurant.setDescription("With its pleasant atmosphere, young and motivated staff and modern interior, the restaurant is one of the most visited and preferred by young people in the city and the surrounding area.");
                                restaurant.setAddress("Str. Yavor 7A, Madan 4900, Bulgaria");
                                restaurant.setPhoneNumber("089 200 2424");
                                restaurant.setEmail("bar_kazablanka@abv.bg");
                                restaurant.setOpen(LocalTime.of(7, 0));
                                restaurant.setClose(LocalTime.of(1, 0));
                                restaurant.setImageUrl("/images/kazablanka/kazablanka.jpg");
                            }
                        }

                        this.restaurantRepository.saveAndFlush(restaurant);
                    });
        }
    }
}