package com.homedelivery.service;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsServiceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        this.userDetailsServiceToTest = new UserDetailsServiceImpl(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound() {

        Role user = new Role();
        user.setName(RoleName.USER);
        Role admin = new Role();
        admin.setName(RoleName.ADMIN);

        User testUser = new User();
        testUser.setUsername("pesheca");
        testUser.setPassword("pesho1234");
        testUser.setFullName("Pesho Petrov");
        testUser.setRoles(List.of(admin, user));

        when(mockUserRepository.findByUsername("pesheca"))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsServiceToTest.loadUserByUsername("pesheca");

        assertInstanceOf(UserDetailsDTO.class, userDetails);

        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) userDetails;

        assertEquals(testUser.getUsername(), userDetailsDTO.getUsername());
        assertEquals(testUser.getPassword(), userDetailsDTO.getPassword());

        List<String> expectedRoles = testUser.getRoles().stream().map(Role::getName).map(r -> "ROLE_" + r).toList();
        List<String> actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceToTest.loadUserByUsername("peshooo"));
    }

}