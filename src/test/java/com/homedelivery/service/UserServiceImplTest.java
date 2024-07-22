package com.homedelivery.service;

import com.homedelivery.model.entity.User;
import com.homedelivery.model.enums.UpdateInfo;
import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.model.user.UserUpdateInfoDTO;
import com.homedelivery.repository.UserRepository;
import com.homedelivery.service.interfaces.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userServiceToTest;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    public void setUp() {
        this.userServiceToTest = new UserServiceImpl(mockApplicationEventPublisher, mockUserRepository,
                mockRoleService, mockPasswordEncoder, mockModelMapper);
    }

    @Test
    public void testRegisterUser_Success() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setFullName("Test User");
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setPassword("topsecret");
        userRegisterDTO.setConfirmPassword("topsecret");

        User user = new User();

        when(mockPasswordEncoder.encode(userRegisterDTO.getPassword()))
                .thenReturn(userRegisterDTO.getPassword()+userRegisterDTO.getPassword());
        when(userServiceToTest.findUserByUsername(userRegisterDTO.getUsername()))
                .thenReturn(Optional.empty());
        when(userServiceToTest.findUserByEmail(userRegisterDTO.getEmail()))
                .thenReturn(Optional.empty());
        when(mockModelMapper.map(userRegisterDTO, User.class)).thenReturn(user);
        when(userServiceToTest.saveAndFlushUser(user)).thenReturn(user);

        boolean result = userServiceToTest.registerUser(userRegisterDTO);

        assertTrue(result);
        assertNotNull(user);
        assertEquals(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword());
        assertEquals(userRegisterDTO.getFullName(), user.getFullName());
        assertEquals(userRegisterDTO.getPassword(), user.getPassword());
        assertEquals(userRegisterDTO.getEmail(), user.getEmail());
    }

    @Test
    public void testRegisterUserUsernameExists() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("test@example.com");

        when(mockUserRepository.findByUsername(userRegisterDTO.getUsername())).thenReturn(Optional.of(new User()));

        boolean result = userServiceToTest.registerUser(userRegisterDTO);

        assertFalse(result);
    }

    @Test
    public void testGetUserDetailsInfo() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockModelMapper.map(user, UserInfoDTO.class)).thenReturn(new UserInfoDTO());

        UserInfoDTO userInfoDTO = userServiceToTest.getUserDetailsInfo(userId);

        assertNotNull(userInfoDTO);
        verify(mockUserRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateUserPropertyUsername() {
        String currentUsername = "currentuser";
        String newUsername = "newuser";
        User user = new User();
        user.setUsername(currentUsername);

        UserUpdateInfoDTO userUpdateInfoDTO = new UserUpdateInfoDTO();
        userUpdateInfoDTO.setUpdateInfo(UpdateInfo.USERNAME);
        userUpdateInfoDTO.setData(newUsername);

        mockSecurityContext(currentUsername);
        when(mockUserRepository.findByUsername(currentUsername)).thenReturn(Optional.of(user));
        when(mockUserRepository.findByUsername(newUsername)).thenReturn(Optional.empty());

        boolean result = userServiceToTest.updateUserProperty(userUpdateInfoDTO);

        assertTrue(result);
        verify(mockUserRepository, times(1)).saveAndFlush(user);
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    public void testFindUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceToTest.findUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceToTest.findUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    public void testFindUserByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceToTest.findUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    private void mockSecurityContext(String username) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
    }

}