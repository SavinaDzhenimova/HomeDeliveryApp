package com.homedelivery.service;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.repository.RoleRepository;
import com.homedelivery.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Optional<Role> findByName(RoleName name) {
        return this.roleRepository.findByName(name);
    }
}