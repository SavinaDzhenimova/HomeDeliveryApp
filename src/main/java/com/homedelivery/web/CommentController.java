package com.homedelivery.web;

import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.model.exportDTO.CommentsViewInfo;
import com.homedelivery.service.interfaces.CommentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ModelAndView viewComments() {
        ModelAndView modelAndView = new ModelAndView("comments");

        CommentsViewInfo commentsViewInfo = this.commentService.getAllComments();

        modelAndView.addObject("comments", commentsViewInfo);

        return modelAndView;
    }

    @GetMapping("/add-comment")
    public ModelAndView addComment(Model model) {

        if (!model.containsAttribute("addCommentDTO")) {
            model.addAttribute("addCommentDTO", new AddCommentDTO());
        }

        return new ModelAndView("add-comment");
    }

    @PostMapping("/add-comment")
    public ModelAndView addComment(@Valid @ModelAttribute("addCommentDTO") AddCommentDTO addCommentDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentDTO", addCommentDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addCommentDTO",
                            bindingResult);

            return new ModelAndView("add-comment");
        }

        boolean isAdded = this.commentService.addComment(addCommentDTO);

        if (isAdded) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully added comment!");

            return new ModelAndView("redirect:/comments");
        }

        return new ModelAndView("add-comment");
    }

    @DeleteMapping("/delete-comment/{id}")
    public ModelAndView deleteComment(@PathVariable("id") Long id) {

        this.commentService.deleteComment(id);

        return new ModelAndView("redirect:/home");
    }
}