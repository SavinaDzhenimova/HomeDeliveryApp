package com.homedelivery.init;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.repository.RoleRepository;
import com.homedelivery.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AdminInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (this.userRepository.count() == 0) {

            User user = new User();
            Optional<Role> optionalAdmin = this.roleRepository.findByName(RoleName.ADMIN);
            Optional<Role> optionalUser = this.roleRepository.findByName(RoleName.USER);

            List<Role> roles = new ArrayList<>();

            if (optionalAdmin.isPresent() && optionalUser.isPresent()) {
                roles.add(optionalAdmin.get());
                roles.add(optionalUser.get());
            }

            user.setUsername("admin");
            user.setFullName("Admin One");
            user.setPhoneNumber("111222333");
            user.setEmail("homedeliverysupportbulgaria@gmail.com");
            user.setAddress("Str. Republika 15, 4900 Madan, Smolyan");
            user.setPassword(this.passwordEncoder.encode("Admin1234"));
            user.setComments(new ArrayList<>());
            user.setOrders(new ArrayList<>());
            user.setRoles(roles);

            this.userRepository.saveAndFlush(user);
        }
    }

}