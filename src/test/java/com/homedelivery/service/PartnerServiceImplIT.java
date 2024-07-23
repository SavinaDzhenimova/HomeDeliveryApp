package com.homedelivery.service;

import com.homedelivery.model.exportDTO.PartnerDetailsDTO;
import com.homedelivery.model.importDTO.AddPartnerDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PartnerServiceImplIT {

    private MockWebServer mockWebServer;

    @Autowired
    private PartnerServiceImpl partnerService;

    @BeforeEach
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8091);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testAddPartner() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        AddPartnerDTO addPartnerDTO = new AddPartnerDTO();

        partnerService.addPartner(addPartnerDTO);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/partners", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testDeletePartner() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        long partnerId = 1L;
        partnerService.deletePartner(partnerId);

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/partners/1", request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testGetAllPartners() throws InterruptedException {
        String responseBody = "[{\"id\":1,\"name\":\"Partner1\"}, {\"id\":2,\"name\":\"Partner2\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json"));

        List<PartnerDetailsDTO> partners = partnerService.getAllPartners();

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/partners", request.getPath());
        assertEquals("GET", request.getMethod());

        assertNotNull(partners);
        assertEquals(2, partners.size());
        assertEquals(1L, partners.get(0).getId());
        assertEquals("Partner1", partners.get(0).getName());
        assertEquals(2L, partners.get(1).getId());
        assertEquals("Partner2", partners.get(1).getName());
    }

}