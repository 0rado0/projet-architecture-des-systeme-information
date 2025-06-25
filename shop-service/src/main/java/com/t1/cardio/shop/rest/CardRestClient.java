package com.t1.cardio.shop.rest;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.t1.cardio.shop.model.Card;


@Component
public class CardRestClient {

    private final WebClient webClient;

    public CardRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://card-service:8083").build();
    }

    public List<Card> getCard(){
        return webClient.get()
                        .uri("cards")
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Card>>() {})
                        .block();
    }

    public List<Card> getCardOnMarketByUserId(Integer userId) {
        return webClient.get()
                        .uri("cards/shop/{userId}", userId)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Card>>() {})
                        .block();
    }

    public Card getCardByID(Integer cardId){
        return webClient.get()
                        .uri("card/{id}",cardId)
                        .retrieve()
                        .bodyToMono(Card.class)
                        .block();
    }

    public void setCardOnMarket(Integer cardId, boolean onMarket){
        webClient.post()
                .uri("card/shop/{cardId}/{onMarket}", cardId, onMarket)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void updateCardUserID(Integer cardId, Integer userId){
        webClient.post()
                .uri("card/{cardId}/{userId}", cardId, userId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}