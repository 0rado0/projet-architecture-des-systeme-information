package com.t1.cardio.card.config;

import com.t1.cardio.card.model.Card;
import com.t1.cardio.card.controller.CardRepository;
import com.t1.cardio.card.controller.CardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.logging.Logger;

/**
 * Configuration pour initialiser automatiquement des cartes au démarrage de l'application
 */
@Configuration
public class CardInitializer {

    private static final Logger LOGGER = Logger.getLogger(CardInitializer.class.getName());

    /**
     * Crée un CommandLineRunner qui s'exécute après le démarrage de l'application 
     * et génère des cartes si nécessaire
     */
    @Bean
    @Order(1)
    public CommandLineRunner initializeCards(CardService cardService, CardRepository cardRepository) {
        return args -> {
            LOGGER.info("Vérification du nombre de cartes existantes...");

            List<Card> existingCards = (List<Card>) cardRepository.findAll();
            int cardCount = existingCards.size();
            LOGGER.info("Nombre de cartes existantes: " + cardCount);

            final int DEFAULT_CARD_COUNT = 1;

            // Si moins de 20 cartes existent, on en génère de nouvelles
            if (cardCount < DEFAULT_CARD_COUNT) {
                int cardsToCreate = DEFAULT_CARD_COUNT - cardCount;
                LOGGER.info("Génération de " + cardsToCreate + " nouvelles cartes...");

                for (int i = 0; i < cardsToCreate; i++) {
                    Card newCard = cardService.generateCard();
                    LOGGER.info("Carte générée: " + newCard.getName() + " (ID: " + newCard.getId() + ")");
                }

                LOGGER.info("Initialisation des cartes terminée. Total des cartes: " + cardRepository.count());
            } else {
                LOGGER.info("Au moins " + DEFAULT_CARD_COUNT + " cartes existent déjà. Aucune nouvelle carte générée.");
            }
        };
    }
}

