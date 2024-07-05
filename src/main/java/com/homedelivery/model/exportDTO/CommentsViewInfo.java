package com.homedelivery.model.exportDTO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentsViewInfo {

    List<CommentDetailsDTO> comments;

    private String totalRating;

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

    public String getTotalRating() {
        Integer totalRating = this.comments.stream()
                .map(CommentDetailsDTO::getRating)
                .reduce(0, Integer::sum);

        DecimalFormat df = new DecimalFormat("##.00");
        String result = df.format((double) totalRating / this.comments.size());

        return (this.comments.size() == 0) ? "0" : result;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }
}