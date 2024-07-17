package com.homedelivery.web.rest;

import com.homedelivery.model.exportDTO.PartnerDetailsDTO;
import com.homedelivery.model.importDTO.AddPartnerDTO;
import com.homedelivery.service.interfaces.PartnerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/partners")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public ModelAndView getAllPartners() {

        ModelAndView modelAndView = new ModelAndView("partners");

        List<PartnerDetailsDTO> partnerDetailsDTOs = this.partnerService.getAllPartners();

        modelAndView.addObject("partners", partnerDetailsDTOs);

        return modelAndView;
    }

    @GetMapping("/add-partner")
    public ModelAndView addPartner(Model model) {

        if (!model.containsAttribute("addPartnerDTO")) {
            model.addAttribute("addPartnerDTO", new AddPartnerDTO());
        }

        return new ModelAndView("add-partner");
    }

    @PostMapping("/add-partner")
    public ModelAndView addPartner(@Valid @ModelAttribute("addPartnerDTO") AddPartnerDTO addPartnerDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPartnerDTO", addPartnerDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addPartnerDTO",
                            bindingResult);

            return new ModelAndView("add-partner");
        }

        this.partnerService.addPartner(addPartnerDTO);

        return new ModelAndView("redirect:/partners");
    }

    @DeleteMapping("/delete-partner/{id}")
    public ModelAndView deletePartner(@PathVariable("id") Long id) {

        this.partnerService.deletePartner(id);

        return new ModelAndView("redirect:/partners");
    }
}
