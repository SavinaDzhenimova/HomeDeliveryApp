package com.homedelivery.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsDTO extends User {

    private Long id;

    private String fullName;

    public UserDetailsDTO(String username,
                          String password,
                          Collection<? extends GrantedAuthority> authorities,
                          Long id,
                          String fullName) {
        super(username, password, authorities);
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}