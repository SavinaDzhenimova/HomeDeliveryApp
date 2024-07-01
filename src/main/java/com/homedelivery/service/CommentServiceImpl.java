package com.homedelivery.service;

import com.homedelivery.model.dto.AddCommentDTO;
import com.homedelivery.model.dto.CommentDetailsDTO;
import com.homedelivery.model.dto.CommentsViewInfo;
import com.homedelivery.model.entity.Comment;
import com.homedelivery.model.entity.User;
import com.homedelivery.repository.CommentRepository;
import com.homedelivery.service.interfaces.CommentService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addComment(AddCommentDTO addCommentDTO, UserDetails userDetails) {

        if (addCommentDTO == null) {
            return false;
        }

        Optional<User> optionalUser = this.userService.findUserByUsername(userDetails.getUsername());

        if (optionalUser.isEmpty()) {
            return false;
        }

        Comment comment = this.modelMapper.map(addCommentDTO, Comment.class);
        comment.setAddedOn(LocalDateTime.now());
        comment.setUser(optionalUser.get());

        this.commentRepository.saveAndFlush(comment);
        return true;
    }

    @Override
    public CommentsViewInfo getAllComments() {

        List<Comment> comments = this.commentRepository.findAll();

        List<CommentDetailsDTO> commentDetailsDTO = comments.stream()
                .map(comment -> {
                    CommentDetailsDTO dto = this.modelMapper.map(comment, CommentDetailsDTO.class);
                    dto.setFullName(comment.getUser().getFullName());
                    dto.setAddedOn(this.parseDate(comment.getAddedOn()));

                    return dto;
                }).toList();

        return new CommentsViewInfo(commentDetailsDTO);
    }

    private String parseDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return date.format(formatter);
    }
}