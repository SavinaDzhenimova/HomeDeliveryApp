package com.homedelivery.web;

import com.homedelivery.model.dto.CommentsViewInfo;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.service.interfaces.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final CommentService commentService;

    public HomeController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        ModelAndView modelAndView = new ModelAndView("home");

        if (userDetails instanceof UserDetailsDTO userDetailsDTO) {
            model.addAttribute("fullName", userDetailsDTO.getFullName());

            CommentsViewInfo commentsViewInfo = this.commentService.getAllCommentsByUser(userDetailsDTO.getUsername());

            modelAndView.addObject("comments", commentsViewInfo);
        }

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about");
    }
}
