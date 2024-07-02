package com.homedelivery.service.interfaces;

import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.model.exportDTO.CommentsViewInfo;

public interface CommentService {

    boolean addComment(AddCommentDTO addCommentDTO, String username);

    CommentsViewInfo getAllComments();

    void deleteComment(Long id, String username);
}