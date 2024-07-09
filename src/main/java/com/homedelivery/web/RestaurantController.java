package com.homedelivery.web;

import com.homedelivery.model.exportDTO.RestaurantDetailsDTO;
import com.homedelivery.model.enums.RestaurantName;
import com.homedelivery.service.interfaces.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/korona")
    public ModelAndView showKoronaInfo(Model model) {

        ModelAndView modelAndView = new ModelAndView("korona");

        RestaurantDetailsDTO restaurantDetailsDTO = this.restaurantService.getRestaurantDetails(RestaurantName.KORONA);

        modelAndView.addObject("restaurant", restaurantDetailsDTO);

        return modelAndView;
    }

    @GetMapping("/vertu")
    public ModelAndView showVertuInfo() {

        ModelAndView modelAndView = new ModelAndView("vertu");

        RestaurantDetailsDTO restaurantDetailsDTO = this.restaurantService.getRestaurantDetails(RestaurantName.VERTU);

        modelAndView.addObject("restaurant", restaurantDetailsDTO);

        return modelAndView;
    }

    @GetMapping("/kazablanka")
    public ModelAndView showKazablankaInfo() {

        ModelAndView modelAndView = new ModelAndView("kazablanka");

        RestaurantDetailsDTO restaurantDetailsDTO = this.restaurantService.getRestaurantDetails(RestaurantName.KAZABLANKA);

        modelAndView.addObject("restaurant", restaurantDetailsDTO);

        return modelAndView;
    }
}