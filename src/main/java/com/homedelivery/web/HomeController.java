package com.homedelivery.web;

import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.service.interfaces.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("home");

        if (userDetails instanceof UserDetailsDTO userDetailsDTO) {
            UserInfoDTO userInfoDTO = this.userService.getUserDetailsInfo(userDetailsDTO.getId());

            modelAndView.addObject("user", userInfoDTO);
            modelAndView.addObject("roles", String.join(", ", userInfoDTO.getRoles()));
            modelAndView.addObject("comments", userInfoDTO.getComments());
            modelAndView.addObject("orders", userInfoDTO.getOrders());
        }

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about() {

        return new ModelAndView("about");
    }
}