package com.homedelivery.service;

import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.model.exportDTO.CommentDetailsDTO;
import com.homedelivery.model.exportDTO.CommentsViewInfo;
import com.homedelivery.model.entity.Comment;
import com.homedelivery.model.entity.User;
import com.homedelivery.repository.CommentRepository;
import com.homedelivery.service.interfaces.CommentService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public boolean addComment(AddCommentDTO addCommentDTO, String username) {

        if (addCommentDTO == null) {
            return false;
        }

        Optional<User> optionalUser = this.userService.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        Comment comment = this.modelMapper.map(addCommentDTO, Comment.class);
        comment.setAddedOn(LocalDateTime.now());
        comment.setUser(user);
        user.getComments().add(comment);

        this.commentRepository.saveAndFlush(comment);
        this.userService.saveAndFlushUser(user);

        return true;
    }

    @Override
    public void deleteComment(Long id, String username) {

        Optional<Comment> optionalComment = this.commentRepository.findById(id);
        Optional<User> optionalUser = this.userService.findUserByUsername(username);

        if (optionalComment.isPresent() && optionalUser.isPresent()) {

            Comment comment = optionalComment.get();
            User user = optionalUser.get();

            if (comment.getUser().getUsername().equals(username)) {
                this.commentRepository.deleteById(id);
            }
        }
    }

    @Override
    public CommentsViewInfo getAllComments() {

        List<Comment> comments = this.commentRepository.findAll();

        List<CommentDetailsDTO> commentDetailsDTO = comments.stream()
                .map(comment -> {
                    CommentDetailsDTO dto = this.modelMapper.map(comment, CommentDetailsDTO.class);
                    dto.setFullName(comment.getUser().getFullName());
                    dto.setAddedOn(comment.parseDateToString(comment.getAddedOn()));

                    return dto;
                }).toList();

        return new CommentsViewInfo(commentDetailsDTO);
    }
}