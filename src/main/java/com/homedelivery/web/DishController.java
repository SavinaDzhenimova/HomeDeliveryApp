package com.homedelivery.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dishes")
public class DishController {


    @GetMapping("/add-dish")
    public ModelAndView addDish() {
        return new ModelAndView("add-dish");
    }

    @GetMapping("/make-order")
    public ModelAndView makeOrder() {
        return new ModelAndView("make-order");
    }

    @GetMapping("/menu")
    public ModelAndView viewMenu() {
        return new ModelAndView("menu");
    }
}
