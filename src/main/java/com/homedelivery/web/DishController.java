package com.homedelivery.web;

import com.homedelivery.model.exportDTO.DishesViewInfo;
import com.homedelivery.model.importDTO.AddDishDTO;
import com.homedelivery.service.interfaces.DishService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        if (isAdded) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully added new dish to menu!");

            return new ModelAndView("redirect:/dishes/menu");
        }

        return new ModelAndView("add-dish");
    }

    @GetMapping("/menu")
    public ModelAndView viewMenu() {

        ModelAndView modelAndView = new ModelAndView("menu");

        DishesViewInfo dishesViewInfo = this.dishService.getAllDishes();

        modelAndView.addObject("dishes", dishesViewInfo);

        return modelAndView;
    }

    @DeleteMapping("/menu/delete-dish/{id}")
    public ModelAndView deleteDish(@PathVariable("id") Long id) {

        this.dishService.deleteDish(id);

        return new ModelAndView("redirect:/dishes/menu");
    }
}
