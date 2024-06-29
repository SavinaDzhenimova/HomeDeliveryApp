package com.homedelivery.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping("/dishes/add-dish")
    public ModelAndView addDish() {
        return new ModelAndView("add-dish");
    }

    @GetMapping("/dishes/make-order")
    public ModelAndView makeOrder() {
        return new ModelAndView("make-order");
    }

    @GetMapping("/dishes/menu")
    public ModelAndView viewMenu() {
        return new ModelAndView("menu");
    }

    @GetMapping("/comments")
    public ModelAndView comments() {
        return new ModelAndView("comments");
    }

    @GetMapping("/comments/add-comment")
    public ModelAndView addComment() {
        return new ModelAndView("add-comment");
    }
}
