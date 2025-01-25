package com.newsaggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NewsApiConfig {

    @Value("${newsapi.key}")
    private String apiKey;

    @Value("${newsapi.base.url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
