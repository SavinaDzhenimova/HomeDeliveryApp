package com.homedelivery.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginError() throws Exception {
        mockMvc.perform(get("/login-error"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("errorMessage", "Invalid username or password!"));
    }

    @Test
    void testRegisterGet() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    void testRegisterPost_Success() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("testuser@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", userRegisterDTO.getUsername())
                        .param("email", userRegisterDTO.getEmail())
                        .param("password", userRegisterDTO.getPassword())
                        .param("confirmPassword", userRegisterDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage", "Successfully registered! Please log in!"));
    }

    @Test
    void testRegisterPost_Failure() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("testuser@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("differentpassword");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", userRegisterDTO.getUsername())
                        .param("email", userRegisterDTO.getEmail())
                        .param("password", userRegisterDTO.getPassword())
                        .param("confirmPassword", userRegisterDTO.getConfirmPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.userRegisterDTO"));
    }

}