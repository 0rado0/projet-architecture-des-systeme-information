package com.t1.cardio.shop.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.t1.cardio.shop.model.AppUser;
import com.t1.cardio.shop.model.UserFund;


@Component
public class UserRestClient {

    private final WebClient webClient;

    public UserRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service:8082").build();
    }

    public void patchUser(Integer userId, double fund){
        webClient.patch()
            .uri("user/funds")
            .bodyValue(new UserFund(userId, fund))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public AppUser getUserById(Integer userId){
        return webClient.get()
                        .uri("user/{id}", userId)
                        .retrieve()
                        .bodyToMono(AppUser.class)
                        .block();
    }
}