package com.homedelivery.web;

import com.homedelivery.model.user.UserDetailsDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails instanceof UserDetailsDTO userDetailsDTO) {
            model.addAttribute("fullName", userDetailsDTO.getFullName());
        }

        return new ModelAndView("home");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }
}
