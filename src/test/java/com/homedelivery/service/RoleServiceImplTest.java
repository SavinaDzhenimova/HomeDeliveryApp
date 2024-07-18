package com.homedelivery.service;

import com.homedelivery.model.entity.Role;
import com.homedelivery.model.enums.RoleName;
import com.homedelivery.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private RoleServiceImpl roleServiceToTest;

    @Mock
    private RoleRepository mockRoleRepository;

    @BeforeEach
    public void setUp() {
        this.roleServiceToTest = new RoleServiceImpl(mockRoleRepository);
    }

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName(RoleName.ADMIN);

        when(mockRoleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleServiceToTest.findByName(RoleName.ADMIN);

        assertEquals(RoleName.ADMIN, foundRole.get().getName());
    }

    @Test
    public void testFindByNameNotFound() {
        Role role = new Role();

        when(mockRoleRepository.findByName(role.getName())).thenReturn(Optional.empty());

        Optional<Role> foundRole = roleServiceToTest.findByName(role.getName());

        assertFalse(foundRole.isPresent());
    }
}