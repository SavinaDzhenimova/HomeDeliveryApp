package com.homedelivery.web;

import com.homedelivery.model.user.UserUpdateInfoDTO;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class UpdateUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateGet() throws Exception {
        mockMvc.perform(get("/users/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("update"))
                .andExpect(model().attributeExists("userUpdateInfoDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePutSuccess() throws Exception {
        when(userService.updateUserProperty(any(UserUpdateInfoDTO.class))).thenReturn(true);

        mockMvc.perform(put("/users/update")
                        .param("updateInfo", "USERNAME")
                        .param("data", "newUsername")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));

        verify(userService).updateUserProperty(any(UserUpdateInfoDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePutValidationError() throws Exception {
        mockMvc.perform(put("/users/update")
                        .param("updateInfo", "USERNAME")
                        .param("data", "no")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update"))
                .andExpect(model().attributeExists("userUpdateInfoDTO"))
                .andExpect(model().attributeHasFieldErrors("userUpdateInfoDTO", "data"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePutServiceError() throws Exception {
        when(userService.updateUserProperty(any(UserUpdateInfoDTO.class))).thenReturn(false);

        mockMvc.perform(put("/users/update")
                        .param("updateInfo", "USERNAME")
                        .param("data", "newUsername")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/update"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(userService).updateUserProperty(any(UserUpdateInfoDTO.class));
    }

}