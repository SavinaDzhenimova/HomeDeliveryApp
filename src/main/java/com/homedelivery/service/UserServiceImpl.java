package com.homedelivery.service;

import com.homedelivery.model.enums.UpdateInfo;
import com.homedelivery.model.exportDTO.CommentDetailsDTO;
import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.model.user.UserUpdateInfoDTO;
import com.homedelivery.repository.UserRepository;
import com.homedelivery.service.interfaces.RoleService;
import com.homedelivery.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public UserInfoDTO getUserDetailsInfo(Long id) {

        Optional<User> optionalUser = this.findUserById(id);

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
    public boolean updateUserProperty(Long id, UserUpdateInfoDTO userUpdateInfoDTO) {

        Optional<User> optionalUser = this.findUserById(id);
        UpdateInfo updateInfo = userUpdateInfoDTO.getUpdateInfo();

        if (optionalUser.isEmpty() || updateInfo == null) {
            return false;
        }

        User user = optionalUser.get();

        switch (updateInfo) {
            case USERNAME -> {
                String newUsername = userUpdateInfoDTO.getData();

                Optional<User> optionalByUsername = this.findUserByUsername(newUsername);

                if (newUsername.length() >= 3 && newUsername.length() <= 20
                        && optionalByUsername.isEmpty()) {
                    user.setUsername(newUsername);
                    this.saveAndFlushUser(user);

                    return true;
                }
            }
            case FULL_NAME -> {
                String newFullName = userUpdateInfoDTO.getData();

                if (newFullName.length() >= 3 && newFullName.length() <= 40) {
                    user.setFullName(newFullName);
                    this.saveAndFlushUser(user);

                    return true;
                }
            }
            case EMAIL -> {
                String newEmail = userUpdateInfoDTO.getData();

                Optional<User> optionalByEmail = this.findUserByEmail(newEmail);

                String regex = "(?<user>^[a-zA-Z0-9]+[-_.]?[a-zA-Z0-9]+)@(?<host>[a-zA-Z]+.+[a-zA-Z]+)$";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(newEmail);

                if (matcher.matches() && optionalByEmail.isEmpty()) {
                    user.setEmail(newEmail);
                    this.saveAndFlushUser(user);

                    return true;
                }
            }
            case ADDRESS -> {
                String newAddress = userUpdateInfoDTO.getData();

                if (newAddress.length() >= 3 && newAddress.length() <= 100) {
                    user.setAddress(newAddress);
                    this.saveAndFlushUser(user);

                    return true;
                }
            }
            case PHONE_NUMBER -> {
                String newPhoneNumber = userUpdateInfoDTO.getData();

                if (newPhoneNumber.length() >= 7 && newPhoneNumber.length() <= 15) {
                    user.setPhoneNumber(newPhoneNumber);
                    this.saveAndFlushUser(user);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return this.userRepository.findById(id);
    }

    private Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void saveAndFlushUser(User user) {
        this.userRepository.saveAndFlush(user);
    }
}