package com.t1.cardio.card.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.t1.cardio.card.model.Card;
import com.t1.cardio.card.model.Card.CardStatus;
import com.t1.cardio.card.model.CardGenerationResponse;

@Service
public class CardUpdaterService {
    private static final Logger logger = Logger.getLogger(CardUpdaterService.class.getName());
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private WebClient gencardWebClient;
    
    @Scheduled(fixedDelay = 5000) // Vérifie toutes les 5 secondes
    public void updateGeneratingCards() {
        List<Card> generatingCards = cardRepository.findByStatus(CardStatus.GENERATING);
        logger.info("Checking " + generatingCards.size() + " cards in generation process");
        
        for (Card card : generatingCards) {
            try {
                Integer taskId = card.getGenerationTaskId();
                if (taskId == null) {
                    logger.warning("Card ID=" + card.getId() + " has no task ID, marking as FAILED");
                    card.setStatus(CardStatus.FAILED);
                    cardRepository.save(card);
                    continue;
                }
                
                logger.info("Checking status for card ID=" + card.getId() + " (task ID=" + taskId + ")");
                CardGenerationResponse response = gencardWebClient.get()
                        .uri("/gencard/" + taskId)
                        .retrieve()
                        .bodyToMono(CardGenerationResponse.class)
                        .block();
                
                if (response == null) {
                    logger.warning("No response for task ID=" + taskId);
                    continue;
                }
                
                if (response.isCompleted()) {
                    logger.info("Task ID=" + taskId + " is completed, updating card ID=" + card.getId());
                    updateCardWithGeneratedData(card, response);
                } else if (response.isFailed()) {
                    logger.warning("Task ID=" + taskId + " failed, marking card ID=" + card.getId() + " as FAILED");
                    card.setStatus(CardStatus.FAILED);
                    cardRepository.save(card);
                } else {
                    logger.info("Task ID=" + taskId + " still in progress, status: " + response.getStatus());
                }
            } catch (Exception e) {
                logger.warning("Error updating card ID=" + card.getId() + ": " + e.getMessage());
            }
        }
    }
    
    private void updateCardWithGeneratedData(Card card, CardGenerationResponse response) {
        String smallImageUrl = response.getImageUrl();
        Map<String, Float> properties = response.getImageProperties();
        
        if (smallImageUrl != null) {
            card.setSmallImgUrl(smallImageUrl);
            card.setImgUrl(String.format("http://tp.cpe.fr:8088/img-service/%s", smallImageUrl));
        }
        
        if (properties != null) {
            if (properties.containsKey("ENERGY")) {
                card.setEnergy(properties.get("ENERGY"));
            }
            
            if (properties.containsKey("HP")) {
                card.setHp(properties.get("HP"));
            }
            
            if (properties.containsKey("DEFENSE")) {
                card.setDefence(properties.get("DEFENSE"));
            }
            
            if (properties.containsKey("ATTACK")) {
                card.setAttack(properties.get("ATTACK"));
            }
        }
        
        // Calcul du prix basé sur les statistiques mises à jour
        float price = (card.getEnergy() * 4) +
                      (card.getHp() * 2) +
                      (card.getDefence() * 3) +
                      (card.getAttack() * 4);
        float minPrice = 50.0f;
        card.setPrice(Math.max(price, minPrice));
        
        card.setStatus(CardStatus.READY);
        
        cardRepository.save(card);
    }
}
