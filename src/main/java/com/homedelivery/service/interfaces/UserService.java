package com.homedelivery.service.interfaces;

import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.model.user.UserUpdateInfoDTO;

import java.util.Optional;

public interface UserService {

    boolean registerUser(UserRegisterDTO userRegisterDTO);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    User saveAndFlushUser(User user);

    boolean updateUserProperty(UserUpdateInfoDTO userUpdateInfoDTO);

    String getLoggedUsername();

    Optional<User> findUserByUsername(String username);

    UserInfoDTO getUserDetailsInfo(Long id);
}