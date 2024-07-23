package com.homedelivery.service.interfaces;

import com.homedelivery.model.entity.Comment;
import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.model.exportDTO.CommentsViewInfo;

import java.util.Optional;

public interface CommentService {

    boolean addComment(AddCommentDTO addCommentDTO);

    CommentsViewInfo getAllComments();

    void deleteComment(Long commentId);

    Optional<Comment> findCommentById(Long id);
}