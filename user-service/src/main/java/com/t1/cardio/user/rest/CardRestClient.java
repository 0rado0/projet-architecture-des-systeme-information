package com.t1.cardio.user.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class CardRestClient {

    private WebClient webClient;

    public CardRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://card-service:8083").build();
    }

    public void giveCardToUser(Integer userId) {
        webClient.post()
                .uri("/card/user/{userId}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        response -> System.out.println("Card given to user with ID: " + userId),
                        error -> System.err.println("Error giving card to user: " + error.getMessage())
                );
    }

}
