package com.homedelivery.model.user;

import com.homedelivery.model.exportDTO.CommentDetailsDTO;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDTO {

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private String password;

    private List<String> roles;

    private List<CommentDetailsDTO> comments;

    public UserInfoDTO() {
        this.roles = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<CommentDetailsDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDetailsDTO> comments) {
        this.comments = comments;
    }
}
