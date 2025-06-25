package com.t1.cardio.card.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CardClient {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://gencard-service:8084") // URL du service d'image
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}


