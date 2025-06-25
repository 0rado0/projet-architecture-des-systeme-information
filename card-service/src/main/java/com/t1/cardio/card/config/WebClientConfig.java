package com.t1.cardio.card.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient gencardWebClient() {
        return WebClient.builder()
                .baseUrl("http://gencard-service:8084") // Même URL que dans CardClient
                .build();
    }
}
