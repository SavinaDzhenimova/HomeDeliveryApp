package com.homedelivery.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CommentsViewInfo {

    List<CommentDetailsDTO> comments;

    public CommentsViewInfo() {
        this.comments = new ArrayList<>();
    }

    public CommentsViewInfo(List<CommentDetailsDTO> comments) {
        this.comments = comments;
    }

    public List<CommentDetailsDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDetailsDTO> comments) {
        this.comments = comments;
    }
}