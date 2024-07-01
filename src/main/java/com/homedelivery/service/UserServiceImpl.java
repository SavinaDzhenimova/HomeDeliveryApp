package com.homedelivery.service;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.repository.UserRepository;
import com.homedelivery.service.interfaces.RoleService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean registerUser(UserRegisterDTO userRegisterDTO) {

        if (userRegisterDTO == null) {
            return false;
        }

        Optional<User> optionalUserByUsername = this.findUserByUsername(userRegisterDTO.getUsername());
        Optional<User> optionalUserByEmail = this.findUserByEmail(userRegisterDTO.getEmail());

        if (optionalUserByUsername.isPresent() || optionalUserByEmail.isPresent()) {
            return false;
        }

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }

        User user = this.modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));

        if (this.userRepository.count() == 0) {
            Optional<Role> optionalAdminRole = this.roleService.findByName(RoleName.ADMIN);
            Optional<Role> optionalUserRole = this.roleService.findByName(RoleName.USER);

            if (optionalAdminRole.isPresent() && optionalUserRole.isPresent()) {
                Set<Role> roles = new HashSet<>();
                roles.add(optionalAdminRole.get());
                roles.add(optionalUserRole.get());

                user.setRoles(roles);
            } else {
                Set<Role> roles = new HashSet<>();
                roles.add(optionalUserRole.get());

                user.setRoles(roles);
            }
        }

        this.saveAndFlushUser(user);
        return true;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    private Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void saveAndFlushUser(User user) {
        this.userRepository.saveAndFlush(user);
    }
}