package com.homedelivery.service.interfaces;

import com.homedelivery.model.dto.AddCommentDTO;
import com.homedelivery.model.dto.CommentsViewInfo;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {

    boolean addComment(AddCommentDTO addCommentDTO, UserDetails userDetails);

    CommentsViewInfo getAllComments();

}