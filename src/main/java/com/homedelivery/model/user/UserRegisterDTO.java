package com.homedelivery.model.user;

import com.homedelivery.model.annotation.ValidEmail;
import com.homedelivery.model.annotation.ValidPassword;
import jakarta.validation.constraints.*;

public class UserRegisterDTO {

    @NotNull
    @Size(min = 3, max = 20, message = "{user_register_username_length}")
    private String username;

    @NotNull
    @Size(min = 3, max = 40, message = "{user_register_full_name_length}")
    private String fullName;

    @NotBlank
    @ValidEmail(message = "{user_register_valid_email_format}")
    private String email;

    @NotNull
    @Size(min = 7, max = 15, message = "{user_register_phone_number_size}")
    private String phoneNumber;

    @NotNull
    @Size(min = 3, max = 100, message = "{user_register_address_size}")
    private String address;

    @NotNull
    @ValidPassword(message = "{user_register_valid_password}")
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