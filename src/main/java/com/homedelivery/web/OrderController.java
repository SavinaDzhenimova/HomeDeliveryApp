package com.homedelivery.web;

import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.service.interfaces.OrderService;
import com.homedelivery.service.interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/make-order")
    public ModelAndView makeOrder(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (!model.containsAttribute("addOrderDTO")) {
            model.addAttribute("addOrderDTO", new AddOrderDTO());
        }

        ModelAndView modelAndView = new ModelAndView("make-order");

        if (userDetails instanceof UserDetailsDTO userDetailsDTO) {
            OrderDishesInfoDTO orderDishesInfoDTO = this.orderService.getAllDishesInCart();

            modelAndView.addObject("username", userDetailsDTO.getUsername());
            modelAndView.addObject("cartDishes", orderDishesInfoDTO);
        }

        OrderDishesInfoDTO orderDishesInfoDTO = this.orderService.getAllDishesInCart();

        modelAndView.addObject("cartDishes", orderDishesInfoDTO);

        return modelAndView;
    }

    @PostMapping("/add-to-cart/{id}")
    public ModelAndView addToCart(@PathVariable("id") Long id) {

        boolean isAdded = this.orderService.addToCart(id);

        return new ModelAndView("redirect:/dishes/menu");
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") Long id) {

        this.orderService.removeDishFromCart(id);

        return "redirect:/orders/make-order";
    }
}
