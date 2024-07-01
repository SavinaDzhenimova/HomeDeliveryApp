package com.homedelivery.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsDTO extends User {

    private String fullName;

    public UserDetailsDTO(String username,
                          String password,
                          Collection<? extends GrantedAuthority> authorities,
                          String fullName) {
        super(username, password, authorities);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}