package com.homedelivery.web;

import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.service.interfaces.DishService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/add-dish")
    public ModelAndView addDish(Model model) {

        if (!model.containsAttribute("addDishDTO")) {
            model.addAttribute("addDishDTO", new AddDishDTO());
        }

        return new ModelAndView("add-dish");
    }

    @PostMapping("/add-dish")
    public ModelAndView addDish(@Valid @ModelAttribute("addDishDTO") AddDishDTO addDishDTO,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addDishDTO", addDishDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addDishDTO",
                            bindingResult);

            return new ModelAndView("add-dish");
        }

        boolean isAdded = this.dishService.addDish(addDishDTO);

        String modelAndView = (isAdded) ? "redirect:/dishes/menu" : "add-dish";

        return new ModelAndView(modelAndView);
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
