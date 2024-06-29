package com.homedelivery.init;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RolesInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RolesInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if (this.roleRepository.count() == 0) {

            Arrays.stream(RoleName.values())
                    .forEach(roleName -> {
                        Role role = new Role();
                        role.setName(roleName);

                        String description = switch (roleName) {
                            case ADMIN -> "Admin can register, login, add dishes to menu, remove dishes from menu, make orders, delete orders, add comments and delete comments.";
                            case USER -> "User can register, login, make orders, delete orders, add comments and delete comments.";
                        };

                        role.setDescription(description);
                        this.roleRepository.saveAndFlush(role);
                    });
        }
    }
}