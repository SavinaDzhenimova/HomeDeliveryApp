package com.homedelivery.web.rest;

import com.homedelivery.model.exportDTO.PartnerDetailsDTO;
import com.homedelivery.model.importDTO.AddPartnerDTO;
import com.homedelivery.service.interfaces.PartnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class PartnerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Test
    void testGetAllPartners_Successful() throws Exception {
        PartnerDetailsDTO partnerDetailsDTO1 = new PartnerDetailsDTO();
        partnerDetailsDTO1.setId(1L);
        partnerDetailsDTO1.setName("Partner1");

        PartnerDetailsDTO partnerDetailsDTO2 = new PartnerDetailsDTO();
        partnerDetailsDTO2.setId(2L);
        partnerDetailsDTO2.setName("Partner2");

        List<PartnerDetailsDTO> partners = Arrays.asList(partnerDetailsDTO1, partnerDetailsDTO2);

        when(partnerService.getAllPartners()).thenReturn(partners);

        mockMvc.perform(get("/partners"))
                .andExpect(status().isOk())
                .andExpect(view().name("partners"))
                .andExpect(model().attributeExists("partners"))
                .andExpect(model().attribute("partners", partners));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddPartner_GetFormPage() throws Exception {
        mockMvc.perform(get("/partners/add-partner"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-partner"))
                .andExpect(model().attributeExists("addPartnerDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testAddPartner() throws Exception {
        AddPartnerDTO addPartnerDTO = new AddPartnerDTO();
        addPartnerDTO.setName("New Partner");

        doNothing().when(partnerService).addPartner(any(AddPartnerDTO.class));

        mockMvc.perform(post("/partners/add-partner")
                        .param("name", "Test Partner")
                        .param("email", "test_partner@example.com")
                        .param("logoUrl", "https://test_partner.jpg")
                        .param("site", "www.test_partner.com")
                        .with(csrf())
                        .flashAttr("addPartnerDTO", addPartnerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/partners"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testDeletePartner() throws Exception {
        long partnerId = 1L;

        doNothing().when(partnerService).deletePartner(partnerId);

        mockMvc.perform(delete("/partners/delete-partner/{id}", partnerId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/partners"));
    }

}