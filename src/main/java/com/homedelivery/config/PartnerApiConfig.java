package com.homedelivery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "offers.api")
public class PartnerApiConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public PartnerApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
