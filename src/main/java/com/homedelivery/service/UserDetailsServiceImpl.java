package com.homedelivery.service;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    private static UserDetails map(User userEntity) {

        return new UserDetailsDTO(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(Role::getName).map(UserDetailsServiceImpl::map).toList(),
                userEntity.getFullName()
        );
    }

    private static GrantedAuthority map(RoleName role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }

}