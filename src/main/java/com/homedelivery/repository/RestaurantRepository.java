package com.homedelivery.repository;

import com.homedelivery.model.entity.Restaurant;
import com.homedelivery.model.enums.RestaurantName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(RestaurantName name);

}