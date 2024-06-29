package com.homedelivery.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController {

    @GetMapping("/comments")
    public ModelAndView comments() {
        return new ModelAndView("comments");
    }

    @GetMapping("/comments/add-comment")
    public ModelAndView addComment() {
        return new ModelAndView("add-comment");
    }
}