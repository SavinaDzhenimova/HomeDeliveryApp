package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.UserInfoDTO;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.user.UserRegisterDTO;

import java.util.Optional;

public interface UserService {

    boolean registerUser(UserRegisterDTO userRegisterDTO);

    void saveAndFlushUser(User user);

    Optional<User> findUserByUsername(String username);

    UserInfoDTO getUserDetailsInfo(String username);

}