package com.homedelivery.web;

import com.homedelivery.model.enums.RoleName;
import com.homedelivery.model.user.UserDetailsDTO;
import com.homedelivery.model.user.UserInfoDTO;
import com.homedelivery.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "test_user", roles = {"USER", "ADMIN"})
    public void testHome_Authenticated() throws Exception {
        GrantedAuthority user = new SimpleGrantedAuthority("ROLE_USER");
        GrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = List.of(user, admin);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                "test_user", "Test1234", authorities, 1L,
                "Test user", "test_user@gmail.com", "Test address", "111222333");

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setRoles(List.of("ROLE_USER"));
        userInfoDTO.setComments(List.of());
        userInfoDTO.setOrders(List.of());

        when(userService.getUserDetailsInfo(anyLong())).thenReturn(userInfoDTO);

        mockMvc.perform(get("/home").with(SecurityMockMvcRequestPostProcessors.user(userDetailsDTO)))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    public void testHome_Anonymous() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    public void testAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

}