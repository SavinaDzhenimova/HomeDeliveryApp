package com.homedelivery.service;

import com.homedelivery.model.enums.UpdateInfo;
import com.homedelivery.service.events.UserRegistrationEvent;
import com.homedelivery.model.exportDTO.CommentDetailsDTO;
import com.homedelivery.model.exportDTO.OrderDetailsDTO;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository,
                           RoleService roleService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
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

        Optional<Role> optionalUserRole = this.roleService.findByName(RoleName.USER);
        List<Role> roles = new ArrayList<>();

        if (optionalUserRole.isPresent()) {
            roles.add(optionalUserRole.get());
            user.setRoles(roles);
        }

        this.applicationEventPublisher.publishEvent(
                new UserRegistrationEvent(this, user.getEmail(), user.getFullName()));

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
                    dto.setAddedOn(this.parseDateToString(comment.getAddedOn()));

                    return dto;
                }).toList();

        List<OrderDetailsDTO> orders = user.getOrders().stream()
                .map(order -> {
                    OrderDetailsDTO dto = this.modelMapper.map(order, OrderDetailsDTO.class);
                    dto.setOrderedOn(this.parseDateToString(order.getOrderedOn()));

                    String deliveredOn = (order.getDeliveredOn() == null)
                            ? "-"
                            : order.parseDateToString(order.getDeliveredOn());

                    dto.setDeliveredOn(deliveredOn);

                    return dto;
                }).toList();

        UserInfoDTO userInfoDTO = this.modelMapper.map(user, UserInfoDTO.class);
        userInfoDTO.setRoles(stringRoles);
        userInfoDTO.setComments(comments);
        userInfoDTO.setOrders(orders);

        return userInfoDTO;
    }

    private String parseDateToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return date.format(formatter);
    }

    @Override
    public boolean updateUserProperty(UserUpdateInfoDTO userUpdateInfoDTO) {

        if (userUpdateInfoDTO == null) {
            return false;
        }

        String username = this.getLoggedUsername();

        Optional<User> optionalUser = this.findUserByUsername(username);
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

                if (isValidEmail(newEmail) && optionalByEmail.isEmpty()) {
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

    private boolean isValidEmail(String newEmail) {

        String regex = "(?<user>^[a-zA-Z0-9]+[-_.]?[a-zA-Z0-9]+)@(?<host>[a-zA-Z]+.+[a-zA-Z]+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newEmail);

        return matcher.matches();
    }

    @Override
    public String getLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User saveAndFlushUser(User user) {
        return this.userRepository.saveAndFlush(user);
    }
}