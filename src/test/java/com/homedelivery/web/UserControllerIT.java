package com.homedelivery.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.homedelivery.model.user.UserRegisterDTO;
import com.homedelivery.service.interfaces.UserService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private GreenMail greenMail;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host,"smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testRegisterGet() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    void testRegisterPost_Success() throws Exception {
        UserRegisterDTO userRegisterDTO = createUserDTO();

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", userRegisterDTO.getUsername())
                        .param("email", userRegisterDTO.getEmail())
                        .param("password", userRegisterDTO.getPassword())
                        .param("fullName", userRegisterDTO.getFullName())
                        .param("phoneNumber", userRegisterDTO.getPhoneNumber())
                        .param("address", userRegisterDTO.getAddress())
                        .param("confirmPassword", userRegisterDTO.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/login"))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(flash().attribute("successMessage", "Successfully registered! Please log in!"));

        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        assertEquals(1, receivedMessages.length);
        MimeMessage registrationMessage = receivedMessages[0];

        assertTrue(registrationMessage.getContent().toString().contains(userRegisterDTO.getFullName()));
        assertEquals(1, registrationMessage.getAllRecipients().length);
        assertEquals(userRegisterDTO.getEmail(), registrationMessage.getAllRecipients()[0].toString());
    }

    @Test
    void testRegisterPost_ErrorInConfirmPassword() throws Exception {
        UserRegisterDTO userRegisterDTO = createUserDTO();
        userRegisterDTO.setConfirmPassword("Wrong1234");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", userRegisterDTO.getUsername())
                        .param("email", userRegisterDTO.getEmail())
                        .param("password", userRegisterDTO.getPassword())
                        .param("fullName", userRegisterDTO.getFullName())
                        .param("phoneNumber", userRegisterDTO.getPhoneNumber())
                        .param("address", userRegisterDTO.getAddress())
                        .param("confirmPassword", userRegisterDTO.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterPost_Failure() throws Exception {
        UserRegisterDTO userRegisterDTO = createUserDTO();
        userRegisterDTO.setFullName("");
        userRegisterDTO.setAddress("no");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", userRegisterDTO.getUsername())
                        .param("email", userRegisterDTO.getEmail())
                        .param("password", userRegisterDTO.getPassword())
                        .param("fullName", userRegisterDTO.getFullName())
                        .param("phoneNumber", userRegisterDTO.getPhoneNumber())
                        .param("address", userRegisterDTO.getAddress())
                        .param("confirmPassword", userRegisterDTO.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "fullName", "address"));
    }

    UserRegisterDTO createUserDTO() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();

        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setEmail("testuser@example.com");
        userRegisterDTO.setPassword("User1234");
        userRegisterDTO.setFullName("Test User");
        userRegisterDTO.setConfirmPassword("User1234");
        userRegisterDTO.setPhoneNumber("111222333");
        userRegisterDTO.setAddress("Test address");

        return userRegisterDTO;
    }

}