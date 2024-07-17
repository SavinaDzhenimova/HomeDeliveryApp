package com.homedelivery.model.importDTO;

import jakarta.validation.constraints.NotNull;

public class AddPartnerDTO {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String site;

    @NotNull
    private String logoUrl;

    public AddPartnerDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}