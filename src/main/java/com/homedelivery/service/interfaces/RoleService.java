package com.homedelivery.service.interfaces;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.enums.RoleName;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(RoleName name);
}
