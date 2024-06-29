package com.homedelivery.service;

import com.homedelivery.model.entity.User;
import com.homedelivery.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository
                .findByUsername(username)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    private static UserDetails map(User user) {

        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of())
                .disabled(false)
                .build();
    }

}
