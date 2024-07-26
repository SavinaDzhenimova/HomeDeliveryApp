package com.homedelivery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "partners.api")
public class PartnersApiConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public PartnersApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

}
