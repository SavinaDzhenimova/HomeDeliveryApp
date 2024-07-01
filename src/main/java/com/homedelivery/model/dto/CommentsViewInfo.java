package com.homedelivery.model.dto;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommentsViewInfo {

    List<CommentDetailsDTO> comments;

    private double totalRating;

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

    public double getTotalRating() throws ParseException {
        Integer totalRating = this.comments.stream()
                .map(CommentDetailsDTO::getRating)
                .reduce(0, Integer::sum);

        DecimalFormat df = new DecimalFormat("##.00");
        String format = df.format((double) totalRating / this.comments.size());
        double result = (double) df.parse(format);

        return (this.comments.size() == 0) ? 0 : result;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }
}