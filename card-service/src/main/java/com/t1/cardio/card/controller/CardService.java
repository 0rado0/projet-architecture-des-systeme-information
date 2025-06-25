package com.t1.cardio.card.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.t1.cardio.card.model.Card;
import com.t1.cardio.card.model.Card.CardStatus;
import com.t1.cardio.card.model.CardGenerationResponse;

@Service
public class CardService {

    @Autowired
    private WebClient webClient;
    
    @Autowired
    private WebClient gencardWebClient;

    @Autowired
    private CardRepository cardRepository;

    public void updateCardUserID(int id, int userId) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.setUserId(userId);
            cardRepository.save(card);
        }
    }

    public void clearCardUserID(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.setUserId(null);
            cardRepository.save(card);
        }
    }

    public Card getCardByID(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        return cardOpt.orElse(null);
    }

    public List<Card> getCardByUserID(int userId) {
        return cardRepository.findByUserId(userId);
    }


    public Card setCardUserID(int id, Card card) {
        if (!cardRepository.existsById(id)) {
            return null;
        }
        card.setId(id);
        return cardRepository.save(card);
    }

    public void delCard(int id) {
        if (!cardRepository.existsById(id)) {
            return;
        }
        cardRepository.deleteById(id);
    }

    public List<Card> getCard() {
        List<Card> cards = new ArrayList<>();
        cardRepository.findAll().forEach(cards::add);
        return cards;
    }

    public void giveCardToUser(int userId) {
        // Récupérer toutes les cartes sans userId
        List<Card> availableCards = cardRepository.findByUserIdIsNull();

        // Si moins de 5 cartes sont disponibles, en générer de nouvelles
        int cardsToGenerate = 5 - availableCards.size();
        if (cardsToGenerate > 0) {
            for (int i = 0; i < cardsToGenerate; i++) {
                // Générer une carte et lui attribuer directement le userId
                Card newCard = generateCard();
                newCard.setUserId(userId);
                cardRepository.save(newCard);
                availableCards.add(newCard);
            }
        }

        // S'il y avait des cartes disponibles sans userId, on les mélange et on en attribue
        if (availableCards.size() > cardsToGenerate) {
            // On enlève les cartes qui viennent d'être créées avec le userId
            List<Card> existingCards = availableCards.subList(0, availableCards.size() - cardsToGenerate);
            Collections.shuffle(existingCards);

            // Nombre de cartes existantes à attribuer
            int cardsToAssign = Math.min(existingCards.size(), 5 - cardsToGenerate);

            // Attribuer le userId aux cartes existantes sélectionnées
            for (int i = 0; i < cardsToAssign; i++) {
                Card card = existingCards.get(i);
                card.setUserId(userId);
                cardRepository.save(card);
            }
        }
    }

    public float calculateCardPrice(Card card) {
        // Formule de calcul du prix basée sur les statistiques
        float price = (card.getEnergy() * 4) +
                (card.getHp() * 2) +
                (card.getDefence() * 3) +
                (card.getAttack() * 4);

        // Prix minimum pour éviter les valeurs négatives ou trop basses
        float minPrice = 50.0f;

        return Math.max(price, minPrice);
    }

    public Card generateCard() {
        // Liste de thèmes pour les cartes
        String[] cardThemes = {
                "Futuristic spaceship",
                "Combat robot",
                "Legendary dragon",
                "Medieval warrior",
                "Powerful mage",
                "Mythical creature",
                "Abyssal monster",
                "Galactic hero",
                "Legendary animal",
                "Pirate ship"
        };

        String[] cardDescriptions = {
                "An unmatched power that can destroy entire planets. This entity is feared across the galaxy for its destructive force.",
                "Fast and agile, this creature is known for its surprise attacks and ability to disappear quickly.",
                "A loyal fighter who defends its territory with honor and bravery. No one has ever defeated it in single combat.",
                "A superior intelligence allows it to predict its opponents' moves and anticipate their strategies.",
                "Master of the fire element, capable of controlling flames and reducing its enemies to ashes.",
                "This rare creature only reveals itself to those who have proven their worth. It is a symbol of power and wisdom.",
                "An ancient guardian that has protected an invaluable treasure for millennia.",
                "Born in the stars, this hero travels the universe to maintain cosmic balance and protect the innocent.",
                "A war machine created by a lost civilization, rediscovered and reactivated for a new era of combat.",
                "This entity feeds on fear and becomes more powerful when facing terrified opponents."
        };

        // Sélectionner aléatoirement un thème et une description
        int themeIndex = (int) (Math.random() * cardThemes.length);
        int descIndex = (int) (Math.random() * cardDescriptions.length);

        String cardName = cardThemes[themeIndex];
        String cardDescription = cardDescriptions[descIndex];

        String shortDescription = cardDescription.length() > 100 ? cardDescription.substring(0, 100) : cardDescription;

        String prompt = "create a 100 word image generation prompt for a card with the following name: '" + cardName +
                "' and description: '" + shortDescription + "'. The image should be colorful and detailed.";

        try {
            // Utiliser gencardWebClient au lieu de webClient avec des paramètres de requête
            CardGenerationResponse response = gencardWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/gencard")
                            .queryParam("prompt", prompt)
                            .build())
                    .retrieve()
                    .bodyToMono(CardGenerationResponse.class)
                    .block();

            if (response == null) {
                throw new RuntimeException("Aucune réponse reçue du service gencard");
            }

            // Définir l'ID de tâche et propriétés depuis la réponse
            Integer taskId = response.getTaskId();
            Map<String, Float> properties = response.getImageProperties() != null ? 
                    response.getImageProperties() : new HashMap<>();
            String smallImageUrl = response.getImageUrl();

            Card card = new Card();
            card.setName(cardName);
            card.setDescription(cardDescription);
            card.setStatus(CardStatus.GENERATING);
            card.setGenerationTaskId(taskId);
            
            // Définir les attributs de base pour l'affichage initial
            if (smallImageUrl != null) {
                card.setSmallImgUrl(smallImageUrl);
                card.setImgUrl(String.format("http://tp.cpe.fr:8088/img-service/%s", smallImageUrl));
            }

            if (properties.containsKey("ENERGY")) card.setEnergy(properties.get("ENERGY"));
            if (properties.containsKey("HP")) card.setHp(properties.get("HP"));
            if (properties.containsKey("DEFENSE")) card.setDefence(properties.get("DEFENSE"));
            if (properties.containsKey("ATTACK")) card.setAttack(properties.get("ATTACK"));

            card.setPrice(calculateCardPrice(card));

            return cardRepository.save(card);
        } catch (Exception e) {
            // En cas d'erreur, créer une carte avec statut FAILED
            Card failedCard = new Card();
            failedCard.setName(cardName);
            failedCard.setDescription(cardDescription);
            failedCard.setStatus(CardStatus.FAILED);
            failedCard.setPrice(50.0f); // Prix minimal par défaut
            
            return cardRepository.save(failedCard);
        }
    }

    public void setCardOnMarket(int id, boolean onMarket) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.setOnMarket(onMarket);
            cardRepository.save(card);
        }
    }

    public List<Card> getCardOnMarket() {
        return cardRepository.findByIsOnMarket(true);
    }

    public List<Card> getCardOnMarketByUserId(int userId) {
        List<Card> cards = cardRepository.findByUserId(userId);
        List<Card> cardsOnMarket = new ArrayList<>();
        for (Card card : cards) {
            if (card.isOnMarket()) {
                cardsOnMarket.add(card);
            }
        }
        return cardsOnMarket;
    }
}