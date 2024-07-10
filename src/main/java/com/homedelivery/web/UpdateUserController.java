package com.homedelivery.web;

import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.model.user.UserUpdateInfoDTO;
import com.homedelivery.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UpdateUserController {

    private final UserService userService;

    public UpdateUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/update")
    public ModelAndView update(Model model) {

        if (!model.containsAttribute("userUpdateInfoDTO")) {
            model.addAttribute("userUpdateInfoDTO", new UserUpdateInfoDTO());
        }

        return new ModelAndView("update");
    }

    @PutMapping("/users/update")
    public ModelAndView update(@Valid @ModelAttribute("userUpdateInfoDTO") UserUpdateInfoDTO userUpdateInfoDTO,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userUpdateInfoDTO", userUpdateInfoDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userUpdateInfoDTO",
                            bindingResult);

            return new ModelAndView("update");
        }

        boolean isUpdated = this.userService.updateUserProperty(userUpdateInfoDTO);

        if (!isUpdated) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Something went wrong! Your changes were NOT saved!");

            return new ModelAndView("redirect:/users/update");
        }

        return new ModelAndView("redirect:/home");
    }
}