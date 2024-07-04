package com.homedelivery.model.user;

import com.homedelivery.model.annotation.ValidEmail;
import com.homedelivery.model.annotation.ValidPassword;
import jakarta.validation.constraints.*;

public class UserRegisterDTO {

    @NotNull
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    @NotNull
    @Size(min = 3, max = 40, message = "Username length must be between 3 and 40 characters!")
    private String fullName;

    @NotBlank(message = "Email cannot be empty!")
    @ValidEmail(message = "Invalid email!")
    private String email;

    @NotNull
    @Size(min = 7, max = 15, message = "Phone number length must be between 7 and 15 characters!")
    private String phoneNumber;

    @NotNull
    @Size(min = 3, max = 100, message = "Address length must be between 3 and 100 characters!")
    private String address;

    @NotNull
    @ValidPassword(message = "Password must contains at least 1 uppercase letter, 1 lowercase letter, 1 digit " +
                    "and must be between 8 and 20 characters long!")
    private String password;

    @NotNull
    private String confirmPassword;

    public UserRegisterDTO() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}