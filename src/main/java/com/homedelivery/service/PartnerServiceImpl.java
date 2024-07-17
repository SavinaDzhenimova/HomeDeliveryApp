package com.homedelivery.service;

import com.homedelivery.model.exportDTO.PartnerDetailsDTO;
import com.homedelivery.model.importDTO.AddPartnerDTO;
import com.homedelivery.repository.PartnerRepository;
import com.homedelivery.service.interfaces.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger LOGGER = LoggerFactory.getLogger(PartnerServiceImpl.class);
    private final RestClient restClient;
    private final PartnerRepository partnerRepository;

    public PartnerServiceImpl(@Qualifier("offersRestClient") RestClient restClient,
                              PartnerRepository partnerRepository) {
        this.restClient = restClient;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public void addPartner(AddPartnerDTO addPartnerDTO) {
        LOGGER.info("Creating new offer...");

        this.restClient
                .post()
                .uri("http://localhost:8091/partners")
                .body(addPartnerDTO)
                .retrieve();
    }

    @Override
    public void deletePartner(long id) {
        LOGGER.info("Delete partner...");

        this.restClient
                .delete()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("http://localhost:8091/partners/delete/{id}")
                                .build(id))
                .retrieve()
                .body(Void.class);
    }

    @Override
    public List<PartnerDetailsDTO> getAllPartners() {
        LOGGER.info("Get all partners...");

        return this.restClient
                .get()
                .uri("http://localhost:8091/partners")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }
}
