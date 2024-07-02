package com.homedelivery.web;

import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login() {

        return new ModelAndView("login");
    }

    @GetMapping("/login-error")
    public ModelAndView loginError(Model model) {

        model.addAttribute("errorMessage", "Invalid username or password!");

        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register(Model model) {

        if (!model.containsAttribute("userRegisterDTO")) {
            model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        }

        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO",
                            bindingResult);

            return new ModelAndView("register");
        }

        boolean isRegistered = this.userService.registerUser(userRegisterDTO);

        if (isRegistered) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully registered! Please Log in!");

            return new ModelAndView("redirect:/users/login");
        }

        return new ModelAndView("register");
    }
}