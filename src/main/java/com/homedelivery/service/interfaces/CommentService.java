package com.homedelivery.service.interfaces;

import com.homedelivery.model.dto.AddCommentDTO;
import com.homedelivery.model.dto.CommentsViewInfo;

public interface CommentService {

    boolean addComment(AddCommentDTO addCommentDTO, String username);

    CommentsViewInfo getAllComments();

    CommentsViewInfo getAllCommentsByUser(String username);

    void deleteComment(Long id, String username);
}