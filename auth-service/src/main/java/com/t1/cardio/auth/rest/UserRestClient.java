package com.t1.cardio.auth.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class UserRestClient {

    private WebClient webClient;

    public UserRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service:8082").build();
    }

    public Mono<Integer> getUserId(String username, String password) {
        return webClient.post()
                .uri("/user/auth")
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(Integer.class)
                .doOnError(error -> System.out.println("Error: " + error.getMessage()));
    }
}
