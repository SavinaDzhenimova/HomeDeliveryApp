package com.homedelivery.service.interfaces;

import com.homedelivery.model.exportDTO.PartnerDetailsDTO;
import com.homedelivery.model.importDTO.AddPartnerDTO;

import java.util.List;

public interface PartnerService {
    void addPartner(AddPartnerDTO addPartnerDTO);

    void deletePartner(long id);

    List<PartnerDetailsDTO> getAllPartners();
}
