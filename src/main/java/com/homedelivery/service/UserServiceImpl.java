package com.homedelivery.service;

import com.homedelivery.model.exportDTO.CommentDetailsDTO;
import com.homedelivery.model.exportDTO.UserInfoDTO;
import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.repository.UserRepository;
import com.homedelivery.service.interfaces.RoleService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
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

        Optional<Role> optionalAdminRole = this.roleService.findByName(RoleName.ADMIN);
        Optional<Role> optionalUserRole = this.roleService.findByName(RoleName.USER);
        List<Role> roles = new ArrayList<>();

        if (this.userRepository.count() == 0) {

            if (optionalAdminRole.isPresent() && optionalUserRole.isPresent()) {
                roles.add(optionalAdminRole.get());
                roles.add(optionalUserRole.get());

                user.setRoles(roles);
            }
        } else {
            if (optionalUserRole.isPresent()) {
                roles.add(optionalUserRole.get());
                user.setRoles(roles);
            }
        }

        this.saveAndFlushUser(user);
        return true;
    }

    @Override
    public UserInfoDTO getUserDetailsInfo(String username) {

        Optional<User> optionalUser = this.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        List<String> stringRoles = user.getRoles().stream()
                .map(role -> role.getName().toString()).toList();
        List<CommentDetailsDTO> comments = user.getComments().stream()
                .map(comment -> {
                    CommentDetailsDTO dto = this.modelMapper.map(comment, CommentDetailsDTO.class);
                    dto.setAddedOn(comment.parseDateToString(comment.getAddedOn()));

                    return dto;
                }).toList();

        UserInfoDTO userInfoDTO = this.modelMapper.map(user, UserInfoDTO.class);
        userInfoDTO.setRoles(stringRoles);
        userInfoDTO.setComments(comments);

        return userInfoDTO;
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