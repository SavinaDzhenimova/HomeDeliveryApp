package com.homedelivery.web;

import com.homedelivery.model.exportDTO.CommentsViewInfo;
import com.homedelivery.model.exportDTO.OrderDishesInfoDTO;
import com.homedelivery.model.exportDTO.OrdersViewInfo;
import com.homedelivery.model.importDTO.AddOrderDTO;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.service.interfaces.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/make-order")
    public ModelAndView viewMakeOrder(Model model, @AuthenticationPrincipal UserDetails userDetails) {

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

    @PostMapping("/make-order/{totalPrice}")
    public ModelAndView makeOrder(@Valid @ModelAttribute("addOrderDTO") AddOrderDTO addOrderDTO,
                                  @PathVariable("totalPrice") BigDecimal totalPrice,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                        "Please, add some dishes to your shopping cart!");

            return new ModelAndView("redirect:/orders/make-order");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOrderDTO", addOrderDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addOrderDTO",
                            bindingResult);

            return new ModelAndView("make-order");
        }

        if (userDetails instanceof UserDetailsDTO userDetailsDTO) {

            boolean isMadeOrder = this.orderService.makeOrder(addOrderDTO, userDetailsDTO, totalPrice);
        }

        return new ModelAndView("redirect:/home");
    }

    @DeleteMapping("/delete-order/{id}")
    public ModelAndView deleteOrder(@PathVariable("id") Long id) {

        this.orderService.deleteOrder(id);

        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/add-to-cart/{id}")
    public ModelAndView addToCart(@PathVariable("id") Long id,
                                  @RequestParam(value = "quantity", defaultValue = "1") int quantity) {

        if (quantity >= 1) {
            this.orderService.addToCart(id, quantity);
        }

        return new ModelAndView("redirect:/dishes/menu");
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") Long id) {

        this.orderService.removeFromCart(id);

        return "redirect:/orders/make-order";
    }

    @GetMapping
    public ModelAndView getAllOrders() {

        ModelAndView modelAndView = new ModelAndView("orders");

        List<OrdersViewInfo> ordersViewInfo = this.orderService.getAllOrders();

        modelAndView.addObject("orders", ordersViewInfo);
        modelAndView.addObject("ordersCount", ordersViewInfo.size());

        return modelAndView;
    }

    @PostMapping("/progress-order/{id}")
    public ModelAndView progressOrder(@PathVariable("id") Long id,
                                      RedirectAttributes redirectAttributes) {

        boolean isProgressed = this.orderService.progressOrder(id);

        if (!isProgressed) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You cannot progress already delivered order!");
        }

        return new ModelAndView("redirect:/orders");
    }

}
